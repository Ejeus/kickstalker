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
public class Tier extends AbstractData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4738583908478953319L;
	private String title;
	private String backers;
	private String body;
	private boolean selected;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getBackers() {
		return backers;
	}
	public void setBackers(String backers) {
		this.backers = backers;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public boolean isSelected() {
		return selected;
	}
	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
}
