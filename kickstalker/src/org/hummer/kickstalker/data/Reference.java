/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.data;

import java.io.Serializable;

import org.hummer.kickstalker.AppController;
import org.hummer.kickstalker.task.RemoteImageDataLoader;
import org.hummer.kickstalker.task.i.TaskCallbackI;

import android.content.Context;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class Reference implements Serializable, Comparable<Reference>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1194023285557190446L;
	private String ref;
	private byte[] image;
	private String label;
	private String imageRef;
	private boolean imageData;

	public Reference(String ref, String label){
		this.ref = ref;
		this.label = label;
		imageData = false;
		imageRef = "";
	}

	public String getRef() {
		return ref;
	}

	public String getLabel() {
		return label;
	}
	
	public void setImageRef(String ref){
		imageRef = ref;
	}
	
	public String getImageRef(){
		return imageRef;
	}
	
	public void setImage(byte[] image){
		this.image = image;
	}

	public void getImageData(Context context, TaskCallbackI callback){
		
		
		if(imageData){
			callback.onTaskFinished(null, image);
			return;
		}

		new RemoteImageDataLoader(null, callback, 
				AppController.getInstance().getImageCache(context), context).execute(imageRef);
		
	}

	@Override
	public String toString(){
		return label;
	}

	@Override
	public boolean equals(Object other){
		if(Reference.class.isInstance(other)){
			return ref.equals(((Reference)other).getRef());
		} else
			return super.equals(other);
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Reference another) {
		return label.compareTo(another.getLabel());
	}

}
