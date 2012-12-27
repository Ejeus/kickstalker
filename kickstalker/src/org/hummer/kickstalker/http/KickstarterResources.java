/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.http;

/**
 * Content resource extraction points on the Kickstarter website.
 * 
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class KickstarterResources {
	
	public static final String TAG_COMMENT_CONTENT = "p";
	
	/**
	 * Id of a project title on a project detail page. 
	 */
	public static final String ID_PROJECT_TITLE = "#title";
	public static final String ID_PROJECT_BACKERS = "#backers_count";
	public static final String ID_PROJECT_PLEDGED = "#pledged";	
	public static final String ID_PROJECT_TIMELEFT = "#project_duration_data";
	
	/**
	 * This is the id of the video area within a specific project page. 
	 */
	public static final String ID_VIDEOSECTION = "div#video-section";
	public static final String ID_BACKED_LIST = "#list";
	
	public static final String ID_UPDATE_LIST = "#main";
	public static final String ID_TIERS_LIST = "#what-you-get";
	
	/**
	 * A handle for a typical project card on kickstarter.
	 */
	public static final String CLASS_PROJECT_CARD = ".project-card";
	public static final String CLASS_PROJECTCARD_IMAGE = "img.projectphoto-little";
	public static final String CLASS_PROJECT_IMAGE = ".overlay";
	public static final String CLASS_BACKED_PROJECT = ".project_item";
	public static final String CLASS_BACKED_PROJECT_NAME = ".project_name";
	
	public static final String CLASS_RECENT_COMMENTS = "ol.comments";
	public static final String CLASS_COMMENT_AUTHOR = "a.author";
	public static final String CLASS_COMMENT_DATE = "span.date";
	
	public static final String CLASS_UPDATE_ENTRY = "div.blogentry";
	public static final String CLASS_UPDATE_TITLE = "h2.title";
	public static final String CLASS_UPDATE_NUMBER = "span.update-number";
	public static final String CLASS_UPDATE_BODY = "div.body";
	public static final String CLASS_UPDATE_BACKERSONLY = "div#for-backers";
	
	public static final String CLASS_TIER = "div.NS-projects-reward";
	public static final String CLASS_TIER_BACKERS = "span.backers-wrap";
	public static final String CLASS_TIER_BODY = "div.desc";
	public static final String CLASS_TIER_SELECTED = "span.you-selected";
	
	
	/**
	 * Class of the short description of a project.
	 */
	public static final String CLASS_PROJECT_SHORTDESCRIPTION = ".short-blurb";
	
	/**
	 * Class of the full description of a project.
	 */
	public static final String CLASS_PROJECT_DESCRIPTION = ".full-description";
	
	/**
	 * Class of the actual video player. Helps to locate the amazon video stream.
	 */
	public static final String CLASS_VIDEOPLAYER = "div.video-player";
	
	/**
	 * Attribute of the video section of a project that indicates whether a project
	 * has a video or not.
	 */
	public static final String ATTR_HASVIDEO = "data-has-video";
	public static final String ATTR_VIDESRC = "data-video";
	
	/**
	 * The actual video player configuration
	 */
	public static final String ATTR_VIDEOCONFIG = "data-video_tag_markup";
	
	public static final String PAGE_DISCOVER = "/discover";
	public static final String PAGE_PROFILE = "/profile";
	public static final String PAGE_COMMENTS = "/comments";
	public static final String PAGE_UPDATES = "/posts";
	
}
