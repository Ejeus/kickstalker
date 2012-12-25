/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker;

import java.io.IOException;
import java.io.StreamCorruptedException;

import org.hummer.kickstalker.cache.HTMLCache;
import org.hummer.kickstalker.cache.ImageCache;
import org.hummer.kickstalker.factory.CacheFactory;

import android.content.Context;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class AppController {

	public static final String SETTINGS_CREDENTIALS = "SETTINGS_CREDENTIALS";
	private static AppController INSTANCE;
	private HTMLCache htmlCache;
	private ImageCache imgCache;
	
	private AppController(){}
	
	public static AppController getInstance(){
		INSTANCE = INSTANCE == null ? new AppController() : INSTANCE;
		return INSTANCE;
	}
	
	public ImageCache getImageCache(Context context){
		if(imgCache==null)
			try {
				imgCache = CacheFactory.loadImageCache(context);
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		
		if(imgCache==null) return new ImageCache();
		return imgCache;
	}
	
	public HTMLCache getHTMLCache(Context context){
		if(htmlCache==null)
			try {
				htmlCache = CacheFactory.loadHTMLCache(context);
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		
		if(htmlCache==null) return new HTMLCache();
		return htmlCache;
	}

}
