/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.factory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OptionalDataException;

import org.hummer.kickstalker.cache.HTMLCache;
import org.hummer.kickstalker.cache.ImageCache;

import android.content.Context;
import android.util.Log;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class CacheFactory {

	public static final String TAG = "CACHEFACTORY";
	public static final String FILENAME_CACHE_IMAGE = "img.cache";
	public static final String FILENAME_CACHE_HTML = "html.cache";
	
	/**
	 * @param context, Context. The current context.
	 * @param cache, HTMLCache. The HTMLCache to persist.
	 * @throws IOException
	 */
	public static void store(Context context, HTMLCache cache) throws IOException{
		AbstractFactory.store(context, cache, FILENAME_CACHE_HTML);
	}
	
	/**
	 * @param context, Context. The current context.
	 * @param cache, ImageCache. The ImageCache to persist.
	 * @throws IOException
	 */
	public static void store(Context context, ImageCache cache) throws IOException{
		AbstractFactory.store(context, cache, FILENAME_CACHE_IMAGE);
	}

	/**
	 * @param context, Context. The current context.
	 * @return HTMLCache. A previously persisted instance of HTMLCache.
	 * @throws OptionalDataException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static HTMLCache loadHTMLCache(Context context) 
			throws OptionalDataException, IOException, ClassNotFoundException{

		HTMLCache htmlCache = new HTMLCache();

		try{
			return (HTMLCache) AbstractFactory.load(context, FILENAME_CACHE_HTML);
		}catch(FileNotFoundException e){
			Log.i(TAG, "Creating fresh HTML Cache instance.");
		}
		
		return htmlCache;

	}

	/**
	 * @param context, Context. The current context.
	 * @return ImageCache. A previously persisted instance of ImageCache.
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws OptionalDataException 
	 */
	public static ImageCache loadImageCache(Context context) 
			throws OptionalDataException, ClassNotFoundException, IOException {

		ImageCache imgCache = new ImageCache();

		try{
			return (ImageCache) AbstractFactory.load(context, FILENAME_CACHE_IMAGE);
		}catch(FileNotFoundException e){
			Log.i(TAG, "Creating fresh Image Cache instance.");
		}
		
		return imgCache;
		
	}

}
