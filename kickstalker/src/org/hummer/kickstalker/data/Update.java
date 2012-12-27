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
public class Update extends AbstractData {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3300309519387727459L;
	private String ref;
	private String title;
	private String updateId;
	private String body;
	public String getRef() {
		return ref;
	}
	public void setRef(String ref) {
		this.ref = ref;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUpdateId() {
		return updateId;
	}
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	
}
