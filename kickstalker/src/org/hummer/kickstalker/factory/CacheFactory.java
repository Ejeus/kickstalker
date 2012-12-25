/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.factory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;

import org.hummer.kickstalker.cache.HTMLCache;
import org.hummer.kickstalker.cache.ImageCache;

import android.content.Context;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class CacheFactory {

	public static final String FILENAME_CACHE_IMAGE = "img.cache";
	public static final String FILENAME_CACHE_HTML = "html.cache";
	
	public static void store(Context context, HTMLCache cache) throws IOException{
		FileOutputStream fos = context.openFileOutput(
				FILENAME_CACHE_HTML, Context.MODE_PRIVATE);
		ObjectOutputStream os = new ObjectOutputStream(fos);
		os.writeObject(cache);
		os.close();
	}
	
	public static void store(Context context, ImageCache cache) throws IOException{
		FileOutputStream fos = context.openFileOutput(
				FILENAME_CACHE_IMAGE, Context.MODE_PRIVATE);
		ObjectOutputStream os = new ObjectOutputStream(fos);
		os.writeObject(cache);
		os.close();
	}

	public static HTMLCache loadHTMLCache(Context context) 
			throws OptionalDataException, IOException, ClassNotFoundException{

		HTMLCache htmlCache;

		try{
			FileInputStream fis = context.openFileInput(FILENAME_CACHE_HTML);
			ObjectInputStream is = new ObjectInputStream(fis);
			htmlCache = (HTMLCache) is.readObject();
			is.close();
		}catch(FileNotFoundException e){
			htmlCache = new HTMLCache();
		}
		
		return htmlCache;

	}

	/**
	 * @param context
	 * @return
	 * @throws IOException 
	 * @throws ClassNotFoundException 
	 * @throws OptionalDataException 
	 */
	public static ImageCache loadImageCache(Context context) 
			throws OptionalDataException, ClassNotFoundException, IOException {

		ImageCache imgCache;

		try{
			FileInputStream fis = context.openFileInput(FILENAME_CACHE_IMAGE);
			ObjectInputStream is = new ObjectInputStream(fis);
			imgCache = (ImageCache) is.readObject();
			is.close();
		}catch(FileNotFoundException e){
			imgCache = new ImageCache();
		}
		
		return imgCache;
		
	}

}
