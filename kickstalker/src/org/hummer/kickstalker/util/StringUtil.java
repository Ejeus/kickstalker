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
		
		String lastTwo = username.substring(username.length()-3,
				username.length()-1);
		String last = username.substring(username.length()-1);
		
		if(lastTwo.equals("'s") || lastTwo.equals("'S")){
			return username;
		} else if("s".equals(last) || "S".equals(last))
			return username + "'";
		else
			return username + "'s";
		
	}
	
}
