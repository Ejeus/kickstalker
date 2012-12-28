/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.data;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class Comment extends AbstractData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7880214282533796883L;
	private Reference author;
	private String date;
	private String content;
	
	public Reference getAuthor() {
		return author;
	}
	public void setAuthor(Reference author) {
		this.author = author;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}	
}
