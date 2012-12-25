/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.data;

import java.io.Serializable;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class Reference implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1194023285557190446L;
	private String ref;
	private byte[] image;
	private String label;

	public Reference(String ref, String label){
		this.ref = ref;
		this.label = label;
	}

	public String getRef() {
		return ref;
	}

	public String getLabel() {
		return label;
	}
	
	public void setImage(byte[] image){
		this.image = image;
	}
	
	public byte[] getImageData(){
		return image;
	}
	
	@Override
	public String toString(){
		return label;
	}
	
}
