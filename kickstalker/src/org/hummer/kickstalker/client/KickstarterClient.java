/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.client;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.hummer.kickstalker.AppController;
import org.hummer.kickstalker.cache.CachedImage;
import org.hummer.kickstalker.cache.CachedPage;
import org.hummer.kickstalker.cache.HTMLCache;
import org.hummer.kickstalker.cache.ImageCache;
import org.hummer.kickstalker.data.Project;
import org.hummer.kickstalker.data.Reference;
import org.hummer.kickstalker.factory.CacheFactory;
import org.hummer.kickstalker.http.KickstarterResources;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

/**
 * The main client for content retrieval from Kickstarter.
 * 
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class KickstarterClient {

	public static final String TAG = "KSCLIENT";
	//30 min cache for HTML files
	public static final long HTMLCACHE_THRESHOLD = 1800000;
	//10 days cache for images (better have them local, real bottleneck)
	public static final long IMGCACHE_THRESHOLD = 864000000;
	public static final String BASE_URL = "https://www.kickstarter.com";
	private HTMLCache htmlCache;
	private ImageCache imgCache;
	
	public KickstarterClient(Context context){
		AppController appC = AppController.getInstance();
		htmlCache = appC.getHTMLCache(context);
		imgCache = appC.getImageCache(context);
		imgCache.drop(IMGCACHE_THRESHOLD);
	}

	/**
	 * @return List<String>. A list of relative references to recently backed projects.<br />
	 * 		(Source is the "Me" menu, "My Backed Projects" part)
	 * @throws IOException 
	 */
	public List<Reference> getDiscoverProjects() throws IOException{

		List<Reference> projectRefs = new ArrayList<Reference>();
		Document doc = Jsoup.connect(BASE_URL + 
				KickstarterResources.PAGE_DISCOVER).get();

		Elements elements = doc.select(KickstarterResources.CLASS_PROJECT_CARD);
		for(Element e : elements){

			Element header = e.select("h2").first();
			Element headerLink = header.select("a").first();
			Reference ref = new Reference(headerLink.attr("href"), headerLink.text());
			
			Element imgtag = e.select(KickstarterResources.CLASS_PROJECTCARD_IMAGE).first();
			String src = imgtag.attr("src");
			ref.setImage(extractImage(src));
			Log.i(TAG, "Loading " + ref.getRef() + ".");
			projectRefs.add(ref);
		}

		return projectRefs;

	}

	public List<Reference> getBackedProjects(String username) throws IOException{
		
		List<Reference> projectRefs = new ArrayList<Reference>();
		
		Document doc = Jsoup.connect(BASE_URL +
				KickstarterResources.PAGE_PROFILE + "/" + username).get();
		
		Element list = doc.select(KickstarterResources.ID_BACKED_LIST).first();
		Elements projects = list.select(KickstarterResources.CLASS_BACKED_PROJECT);
		
		for(Element project : projects){
			String link = project.attr("href");
			String name = project.select(
					KickstarterResources.CLASS_BAKCED_PROJECT_NAME).first().text();
			
			Reference ref = new Reference(link, name);
			String src = project.select("img").first().attr("src");
			ref.setImage(extractImage(src));
			projectRefs.add(ref);
		}
		
		return projectRefs;
		
	}

	private Document getResource(HTMLCache cache, Reference reference,
			Activity activity, boolean persistImmediately) throws IOException{

		String ref = reference.getRef();

		if(cache.containsKey(ref)){
			return Jsoup.parse(cache.get(ref).getHTML());
		} else {
			Document doc = Jsoup.connect(BASE_URL + reference.getRef()).get();
			CachedPage page = new CachedPage();
			page.setReference(ref);
			page.setHTML(doc.html());
			cache.put(ref, page);
			
			if(persistImmediately) CacheFactory.store(activity, cache);
			return doc;
		}

	}

	/**
	 * @param reference
	 * @return
	 * @throws IOException 
	 */
	public Project getProject(Document doc, Reference reference) throws IOException {

		Project project = new Project(reference.getRef());

		Elements meta = doc.select("meta");
		project.setTitle(meta.select("meta[property=og:title]").
				first().attr("content"));
		project.setShortDescription(meta.select("meta[property=og:description").
				first().attr("content"));

		project.setDescription(doc.select(
				KickstarterResources.CLASS_PROJECT_DESCRIPTION).first().html());

		String backers = doc.select(
				KickstarterResources.ID_PROJECT_BACKERS).
				first().attr("data-backers-count");
		project.setBackers(Float.valueOf(backers).intValue());

		Element pledged = doc.select(
				KickstarterResources.ID_PROJECT_PLEDGED).
				first();

		project.setPledged(Float.valueOf(pledged.attr("data-pledged")).intValue());
		project.setPercent(Float.valueOf(pledged.attr("data-percent-raised")));
		project.setGoal(Float.valueOf(pledged.attr("data-goal")).intValue());

		String timeLeft = doc.select(
				KickstarterResources.ID_PROJECT_TIMELEFT).
				first().attr("data-hours-remaining");
		project.setTimeLeft(Float.valueOf(timeLeft).intValue());

		String imgRef = meta.select("meta[property=og:image]").first().attr("content");
		Log.i(TAG, "Requesting image at " + imgRef);
		project.setImageData(extractImage(imgRef));
		return project;

	}


	/**
	 * @param first
	 * @return
	 */
	private byte[] extractImage(String ref) {
		
		if(imgCache.containsKey(ref)) return imgCache.get(ref).getData();
		
		try {
			URL url = new URL(ref);
			InputStream is = url.openStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			for(int b; (b = is.read()) != -1;){
				baos.write(b);
			}

			byte[] returnVal = baos.toByteArray();
			baos.close();
			is.close();
			
			//cache image
			CachedImage ci = new CachedImage();
			ci.setReference(ref);
			ci.setData(returnVal);
			imgCache.put(ref, ci);
			return returnVal;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return new byte[0];
	}


	/**
	 * @param activity, BaseActivity - The context to use. 
	 * @param refs, List<Reference> - The project references to load
	 * @return List<Project>
	 */
	public List<Project> getProjectsFromRef(Activity activity, List<Reference> refs) {

		htmlCache.drop(HTMLCACHE_THRESHOLD);
		
		List<Project> projects = new ArrayList<Project>();

		try {
			for(int i=0, l=refs.size();i<l;i++){
				Reference ref = refs.get(i);
				projects.add(getProject(getResource(htmlCache, ref, activity, false), ref));
			}

			CacheFactory.store(activity, htmlCache);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return projects;

	}
	
	public Project getProjectFromRef(Activity activity, Reference ref) throws IOException{
		return getProject(getResource(htmlCache, ref, activity, true), ref);
	}
	
	public void persistCache(Context context) throws IOException{
		CacheFactory.store(context, htmlCache);
		CacheFactory.store(context, imgCache);
	}

}
