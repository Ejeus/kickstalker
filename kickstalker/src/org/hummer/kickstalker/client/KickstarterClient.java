/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hummer.kickstalker.AppController;
import org.hummer.kickstalker.cache.CachedPage;
import org.hummer.kickstalker.cache.HTMLCache;
import org.hummer.kickstalker.cache.ImageCache;
import org.hummer.kickstalker.data.Comment;
import org.hummer.kickstalker.data.Project;
import org.hummer.kickstalker.data.Reference;
import org.hummer.kickstalker.data.Tier;
import org.hummer.kickstalker.data.Update;
import org.hummer.kickstalker.factory.CacheFactory;
import org.hummer.kickstalker.http.KickstarterResources;
import org.hummer.kickstalker.util.MediaUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.content.Context;

/**
 * The main client for content retrieval from Kickstarter. This is the main
 * logic for querying and caching Kickstarter content.
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
	private SearchBroker searchBroker;
	private Context context;
	private HTMLCache htmlCache;
	private ImageCache imgCache;

	public KickstarterClient(Context context){
		AppController appC = AppController.getInstance();
		searchBroker = new SearchBroker();
		this.context = context;
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
		buildProjectsFromCard(projectRefs, elements);

		CacheFactory.store(context, imgCache);
		return projectRefs;

	}

	public List<Reference> getBackedProjects(String username) throws IOException{

		List<Reference> projectRefs = new ArrayList<Reference>();

		Document doc = Jsoup.connect(BASE_URL +
				KickstarterResources.PAGE_PROFILE + "/" + username).get();

		Element list = doc.select(KickstarterResources.ID_BACKED_LIST).first();
		if(list==null) return projectRefs;
		
		Elements projects = list.select(KickstarterResources.CLASS_BACKED_PROJECT);

		for(Element project : projects){
			String link = project.attr("href");
			String name = project.select(
					KickstarterResources.CLASS_BACKED_PROJECT_NAME).first().text();

			Reference ref = new Reference(link, name);
			String src = project.select("img").first().attr("src");
			ref.setImageRef(src);
			projectRefs.add(ref);
		}

		return projectRefs;

	}
	
	public List<Reference> getProjectsFor(String searchTerm) throws IOException{
		
		List<Reference> projects = new ArrayList<Reference>();
		
		Document doc = searchBroker.search(searchTerm);
		Elements elements = doc.select(KickstarterResources.CLASS_PROJECT_CARD);
		buildProjectsFromCard(projects, elements);
		
		return projects;
		
	}
	
	protected void buildProjectsFromCard(List<Reference> projectRefs, Elements elements){
		for(Element e : elements){

			Element header = e.select("h2").first();
			Element headerLink = header.select("a").first();
			Reference ref = new Reference(headerLink.attr("href"), headerLink.text());

			Element imgtag = e.select(KickstarterResources.CLASS_PROJECTCARD_IMAGE).first();
			String src = imgtag.attr("src");
			ref.setImageRef(src);
			projectRefs.add(ref);
		}
	}

	public List<Tier> getTiersFor(Context context, String projectRef) throws IOException{

		if(projectRef.contains("?"))
			projectRef = projectRef.substring(0, projectRef.indexOf("?"));
		List<Tier> tiers = new ArrayList<Tier>();

		HTMLCache cache = AppController.getInstance().getHTMLCache(context);

		Document doc = getResource(cache, projectRef, context, true);

		Element list = doc.select(KickstarterResources.ID_TIERS_LIST).first();
		if(list==null) return tiers;

		Elements entries = list.select("li");

		for(Element entry : entries){

			Tier tier = new Tier();
			tier.setTitle(entry.select("h3").text());
			tier.setBackers(entry.select(
					KickstarterResources.CLASS_TIER_BACKERS).first().text());

			tier.setSelected(entry.select(
					KickstarterResources.CLASS_TIER_SELECTED).size()>0);

			tier.setBody(entry.select(
					KickstarterResources.CLASS_TIER_BODY).first().html());

			tiers.add(tier);

		}

		return tiers;

	}

	public List<Update> getUpdatesFor(Context context, String projectRef) 
			throws IOException{

		if(projectRef.contains("?"))
			projectRef = projectRef.substring(0, projectRef.indexOf("?"));
		List<Update> updates = new ArrayList<Update>();

		HTMLCache cache = AppController.getInstance().getHTMLCache(context);

		String ref = projectRef + KickstarterResources.PAGE_UPDATES;
		Document doc = getResource(cache, ref, context, true);
		Element list = doc.select(KickstarterResources.ID_UPDATE_LIST).first();
		if(list==null) return updates;

		Elements entries = list.select(KickstarterResources.CLASS_UPDATE_ENTRY);
		for(Element entry : entries){

			Update update = new Update();
			update.setRef(ref);
			update.setTitle(entry.select(
					KickstarterResources.CLASS_UPDATE_TITLE).first().text());

			update.setUpdateId(entry.select(
					KickstarterResources.CLASS_UPDATE_NUMBER).first().text());


			Element body = entry.select(KickstarterResources.CLASS_UPDATE_BODY).first();

			if(body==null){
				update.setBody(entry.select(
						KickstarterResources.CLASS_UPDATE_BACKERSONLY).first().html());
			} else {
				update.setBody(body.html());
			}

			updates.add(update);

		}

		return updates;

	}

	/**
	 * @param context, Context. The current activity on the front end.
	 * @param projectRef, String. The referrer on Kickstarter to the project.
	 * @return List<Comment>. A list of available recent comments on Kickstarter.
	 * @throws IOException
	 */
	public List<Comment> getCommentsFor(Context context, String projectRef) 
			throws IOException{

		if(projectRef.contains("?"))
			projectRef = projectRef.substring(0, projectRef.indexOf("?"));
		List<Comment> comments = new ArrayList<Comment>();

		HTMLCache cache = AppController.getInstance().getHTMLCache(context);

		Document doc = getResource(cache, projectRef + 
				KickstarterResources.PAGE_COMMENTS, context, true);

		Element list = doc.select(KickstarterResources.CLASS_RECENT_COMMENTS).first();

		if(list==null) return comments;
		Elements entries = list.select("li");

		for(Element entry : entries){
			Comment comment = new Comment();
			Element author = entry.select(
					KickstarterResources.CLASS_COMMENT_AUTHOR).first();
			
			String href = author.attr("href");
			String username = href.substring(href.lastIndexOf("/"), href.length());
			Reference authorRef = new Reference(username, author.text());
			comment.setAuthor(authorRef);

			comment.setDate(entry.select(
					KickstarterResources.CLASS_COMMENT_DATE).first().text());

			comment.setContent(entry.select(
					KickstarterResources.TAG_COMMENT_CONTENT).first().html());

			comments.add(comment);
		}

		return comments;

	}

	/**
	 * @param cache, HTMLCache. Current cache object.
	 * @param ref, String. The HTTP referred to draw from if necessary.
	 * @param context, Context. The current context on frontend.
	 * @param persistImmediately, boolean. State true if result should be hardcached.
	 * @return Document. The HTML Document that was returned from cache or remotely.
	 * @throws IOException
	 */
	private Document getResource(HTMLCache cache, String ref,
			Context context, boolean persistImmediately) throws IOException{

		long checkstamp = new Date().getTime() - HTMLCACHE_THRESHOLD;
		if(cache.containsKey(ref) && cache.get(ref).getCacheStamp()>=checkstamp){
			return Jsoup.parse(cache.get(ref).getHTML());
		} else {
			Document doc = Jsoup.connect(BASE_URL + ref).
					userAgent("Mozilla/5.0 (Windows NT 6.1; WOW64) " +
							"AppleWebKit/535.2 (KHTML, like Gecko) " +
							"Chrome/15.0.874.120 Safari/535.2").get();
			
			CachedPage page = new CachedPage();
			page.setReference(ref);
			page.setHTML(optimizeContent(doc));
			cache.put(ref, page);

			if(persistImmediately) CacheFactory.store(context, cache);
			return doc;
		}

	}

	/**
	 * @param reference, Reference. The project reference to get full details for.
	 * @return Project. A full fledged data object for the project reference.
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
		
		project.setCurrency(pledged.select("data").first().attr("data-currency"));
		Element creator = doc.select(KickstarterResources.ID_PROJECT_CREATOR).first().
				select("a").first();
		project.setOwner(createOwnerReference(creator));

		String timeLeft = doc.select(
				KickstarterResources.ID_PROJECT_TIMELEFT).
				first().attr("data-hours-remaining");
		project.setTimeLeft(Float.valueOf(timeLeft).intValue());

		String imgRef = meta.select("meta[property=og:image]").first().attr("content");
		project.setImageData(MediaUtil.extractImage(context, imgRef, imgCache));
		loadVideoConfig(doc, project);
		//CacheFactory.store(context, imgCache);
		return project;

	}


	/**
	 * @param creator
	 * @return
	 */
	private Reference createOwnerReference(Element creator) {
		
		String label = creator.text();
		String ref = creator.attr("href").split("/")[2];
		return new Reference(ref, label);
		
	}
	
	protected String optimizeContent(Document doc){
		
		doc.select("iframe").remove();
		
		Elements imgs = doc.select("img");
		for(Element img : imgs){
			img.attr("height", "");
			img.attr("width", "300");
		}
		
		return doc.html();
	}
	
	public void loadVideoConfig(Document doc, Project prj){
		
		Element section = doc.select(KickstarterResources.ID_VIDEOSECTION).first();
		
		if(section.attr(KickstarterResources.ATTR_HASVIDEO).equals("true")){
			
			Element player = section.select(
					KickstarterResources.CLASS_VIDEOPLAYER).first();
			
			prj.setHasVideo(true);
			Reference videoRef = new Reference(
					player.attr(KickstarterResources.ATTR_VIDESRC),
					prj.getTitle());
			prj.setVideoReference(videoRef);
			
		} else {
			
			prj.setHasVideo(false);
			
		}
		
	}


	/**
	 * @param context, Context - The context to use. 
	 * @param refs, List<Reference> - The project references to load
	 * @return List<Project>
	 */
	public List<Project> getProjectsFromRef(Context context, List<Reference> refs) {

		htmlCache.drop(HTMLCACHE_THRESHOLD);

		List<Project> projects = new ArrayList<Project>();

		try {
			for(int i=0, l=refs.size();i<l;i++){
				Reference ref = refs.get(i);
				projects.add(getProject(getResource(htmlCache, 
						ref.getRef(), context, false), ref));
			}

			CacheFactory.store(context, htmlCache);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return projects;

	}

	public Project getProjectFromRef(Context context, Reference ref) throws IOException{
		return getProject(getResource(htmlCache, ref.getRef(), context, true), ref);
	}

	public void persistCache(Context context) throws IOException{
		CacheFactory.store(context, htmlCache);
		CacheFactory.store(context, imgCache);
	}

}
