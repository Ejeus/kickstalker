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
	
	/**
	 * Id of a project title on a project detail page. 
	 */
	public static final String ID_PROJECT_TITLE = "#title";
	
	/**
	 * This is the id of the video area within a specific project page. 
	 */
	public static final String ID_PROJECT_VIDEOSECTION = "#video-section";
	
	/**
	 * A handle for a typical project card on kickstarter.
	 */
	public static final String CLASS_PROJECT_CARD = ".project-card";
	
	public static final String CLASS_PROJECT_IMAGE = ".overlay";
	
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
	public static final String CLASS_VIDEOPLAYER = ".video-player";
	
	/**
	 * Attribute of the video section of a project that indicates whether a project
	 * has a video or not.
	 */
	public static final String ATTR_HASVIDEO = "data-has-video";
	
	/**
	 * The actual video player configuration
	 */
	public static final String ATTR_VIDEOCONFIG = "data-video_tag_markup";
	
	public static final String PAGE_DISCOVER = "discover";
	
}
