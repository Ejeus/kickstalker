/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.data;

import java.util.ArrayList;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class BookmarkBundle extends ArrayList<Reference> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4659444340729248865L;
	
	public enum BookmarkType{
		PROJECT,
		PERSON
	}
	
	private BookmarkType type;
	
	public void setBookmarkType(BookmarkType type){
		this.type = type;
	}
	
	public BookmarkType getBookmarkType(){
		return type;
	}
}
