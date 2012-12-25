/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class HTMLCache extends HashMap<String, CachedPage> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2158059938631117616L;
	long lastSelectiveFlush = 0;
	
	/**
	 * @return long. A timestamp that was the last time a drop() was executed.
	 */
	public long getLastSelectiveFlush(){
		return lastSelectiveFlush;
	}
	
	/**
	 * @param threshold, long. The amount of time into the past to keep cached
	 * 		documents for.
	 */
	public void drop(long threshold){
		
		long now = new Date().getTime();
		long hardLimit = now - threshold;
		Collection<String> trash = new ArrayList<String>();
		
		for(String key : keySet()){
			CachedPage page = get(key);
			if(page.getCacheStamp()<hardLimit)
				trash.add(key);
		}
		
		for(String key : trash)
			remove(key);
		
		lastSelectiveFlush = now;
		
	}
	
	/**
	 * Removes all content from the cache.
	 */
	public void flush(){
		clear();
	}
	
}
