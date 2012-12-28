/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.util;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class StringUtil {

	public static String getOwnership(String username){
		
		char c = username.charAt(username.length()-1);
		
		if("s".equals(c) || "S".equals(c))
			return username + "'";
		else
			return username + "'s";
		
	}
	
}
