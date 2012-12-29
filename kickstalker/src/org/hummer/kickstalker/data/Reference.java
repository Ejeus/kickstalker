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
public class Reference implements Serializable, Comparable<Reference>{

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
