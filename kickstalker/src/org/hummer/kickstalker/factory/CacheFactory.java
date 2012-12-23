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
import java.io.StreamCorruptedException;

import org.hummer.kickstalker.ProjectCache;

import android.content.Context;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class CacheFactory {

	public static final String FILENAME_CACHE_PROJECT = "project.cache";

	public static void store(Context context, ProjectCache cache) throws IOException{
		FileOutputStream fos = context.openFileOutput(
				FILENAME_CACHE_PROJECT, Context.MODE_PRIVATE);
		ObjectOutputStream os = new ObjectOutputStream(fos);
		os.writeObject(cache);
		os.close();
	}

	public static ProjectCache loadProjectCache(Context context) 
			throws StreamCorruptedException, IOException, ClassNotFoundException{

		ProjectCache pcache;

		try{
			FileInputStream fis = context.openFileInput(FILENAME_CACHE_PROJECT);
			ObjectInputStream is = new ObjectInputStream(fis);
			pcache = (ProjectCache) is.readObject();
			is.close();
		}catch(FileNotFoundException e){
			pcache = new ProjectCache();
		}
		
		return pcache;

	}

}
