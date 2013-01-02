/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker;

import java.io.IOException;
import java.io.StreamCorruptedException;

import org.hummer.kickstalker.cache.HTMLCache;
import org.hummer.kickstalker.cache.ImageCache;
import org.hummer.kickstalker.data.BookmarkBundle;
import org.hummer.kickstalker.data.BookmarkBundle.BookmarkType;
import org.hummer.kickstalker.data.Configuration;
import org.hummer.kickstalker.factory.BaseFactory;
import org.hummer.kickstalker.factory.BookmarkFactory;
import org.hummer.kickstalker.factory.CacheFactory;

import android.content.Context;

/**
 * Central application controller core that provides hooks to 
 * root elements of the application
 * 
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class AppController {

	public static final String CONFIGURATION_FILENAME = "appconfig.cfg";
	private static AppController INSTANCE;
	private HTMLCache htmlCache;
	private ImageCache imgCache;
	private Configuration config;
	
	private AppController(){}
	
	public static AppController getInstance(){
		INSTANCE = INSTANCE == null ? new AppController() : INSTANCE;
		return INSTANCE;
	}
	
	/**
	 * @param context, Context. Current context to draw config for.
	 * @return Configuration. This apps base configuration.
	 */
	public Configuration getConfig(Context context){
		
		if(config==null){
			try{
				config = (Configuration) 
						BaseFactory.load(context, CONFIGURATION_FILENAME);
			}catch(Exception e){
				config = new Configuration();
			}
		}
		
		return config;
	}
	
	/**
	 * @param context, Context. Current context to ImageCache for.
	 * @return ImageCache.
	 */
	public ImageCache getImageCache(Context context){
		
		if(imgCache==null)
			try {
				imgCache = CacheFactory.loadImageCache(context);
			} catch (Exception e) {
				imgCache = new ImageCache();
			}
		
		return imgCache;
	}
	
	/**
	 * @param context, Context. Current context to draw HTMLCache for.
	 * @return HTMLCache.
	 */
	public HTMLCache getHTMLCache(Context context){
		
		if(htmlCache==null)
			try {
				htmlCache = CacheFactory.loadHTMLCache(context);
			} catch (Exception e) {
				htmlCache = new HTMLCache();
			}
		
		return htmlCache;
	}
	
	/**
	 * @param context, Context. Current context to draw bookmarks for
	 * @param type, BookmarkType. The type of bookmarks to load.
	 * @return BookmarkBundle.
	 */
	public BookmarkBundle getBookmarks(Context context, BookmarkType type){
		
		BookmarkBundle bmb=null;
		try {
			bmb = BookmarkFactory.load(context, type);
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		if(bmb==null){ 
			bmb = new BookmarkBundle();
			bmb.setBookmarkType(type);
		}

		return bmb;
		
	}

	/**
	 * This action is mainly used by the ConfigurationActivity
	 * 
	 * @param context, Context. The current context to persist config for.
	 */
	public void persistConfig(Context context) {
		try {
			BaseFactory.store(context, config, CONFIGURATION_FILENAME);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

}
