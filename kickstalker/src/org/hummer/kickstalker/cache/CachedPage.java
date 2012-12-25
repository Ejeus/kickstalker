/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.cache;

import java.util.Date;

import org.hummer.kickstalker.cache.i.Cachable;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class CachedPage implements Cachable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6332423807957762590L;
	private String ref;
	private String html;
	private long cacheStamp;
	
	public CachedPage(){
		stamp();
	}
	
	public void setReference(String ref){
		this.ref = ref;
	}
	
	public String getReference(){
		return ref;
	}
	
	public void setHTML(String html){
		this.html = html;
		stamp();
		
	}
	
	public String getHTML(){
		return html;
	}
	
	public long getCacheStamp(){
		return cacheStamp;
	}
	
	private void stamp(){
		cacheStamp = new Date().getTime();
	}
	
}
