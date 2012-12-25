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

import org.hummer.kickstalker.ProjectCache;
import org.hummer.kickstalker.activity.BaseActivity;
import org.hummer.kickstalker.data.Project;
import org.hummer.kickstalker.data.Reference;
import org.hummer.kickstalker.factory.CacheFactory;
import org.hummer.kickstalker.http.KickstarterResources;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
	public static final String BASE_URL = "https://www.kickstarter.com/";


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
		for(Element e : elements.select("h2")){

			Element headerLink = e.select("a").first();
			Reference ref = new Reference(headerLink.attr("href"), headerLink.text());
			projectRefs.add(ref);
		}

		return projectRefs;

	}


	/**
	 * @param reference
	 * @return
	 * @throws IOException 
	 */
	public Project getProject(Reference reference) throws IOException {

		Project project = new Project();
		project.setRef(reference.getRef());

		Document doc = Jsoup.connect(BASE_URL + reference.getRef()).get();

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
		project.setImageData(extractImage(imgRef));
		return project;

	}


	/**
	 * @param first
	 * @return
	 */
	private byte[] extractImage(String ref) {

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
	public List<Project> getProjectsFromRef(BaseActivity activity, List<Reference> refs) {

		ProjectCache cache = activity.getAppController().getProjectCache(activity);
		List<Project> projects = new ArrayList<Project>();

		try {
			for(int i=0, l=refs.size();i<l;i++){
				Reference ref = refs.get(i);
				if(cache.containsKey(ref.getRef())){
					projects.add(cache.get(ref.getRef()));
				} else {
					Project prj = getProject(refs.get(i));
					projects.add(prj);
					cache.put(ref.getRef(), prj);
				}
			}

			CacheFactory.store(activity, cache);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return projects;

	}

}
