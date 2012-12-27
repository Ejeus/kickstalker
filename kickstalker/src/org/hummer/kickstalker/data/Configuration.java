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
public class Configuration extends AbstractData {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4782955102863038925L;
	static final String DEV_PROFILE = "charnode";
	private String username;

	public Configuration(){
		super();
		username = DEV_PROFILE;
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
}
