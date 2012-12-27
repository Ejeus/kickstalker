/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class AbstractData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7678646852127405345L;
	private long stamp;
	
	public AbstractData(){
		stamp = new Date().getTime();
	}
	
	public long getCreationStamp(){
		return stamp;
	}
	
}
