/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.cache;

import java.util.Date;

import org.hummer.kickstalker.cache.i.Cachable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class CachedImage implements Cachable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7073273306074439184L;
	private String ref;
	private byte[] data;
	private long cacheStamp;
	
	public CachedImage(){
		stamp();
	}
	
	public void setReference(String ref){
		this.ref = ref;
	}
	
	public String getReference(){
		return ref;
	}
	
	public void setData(byte[] data){
		this.data = data;
		stamp();
	}
	
	public byte[] getData(){
		return data;
	}
	
	public Bitmap getAsImage(){
		return BitmapFactory.decodeByteArray(data, 0, data.length);
	}
	
	public long getCacheStamp(){
		return cacheStamp;
	}
	
	private void stamp(){
		cacheStamp = new Date().getTime();
	}
	
	
}
