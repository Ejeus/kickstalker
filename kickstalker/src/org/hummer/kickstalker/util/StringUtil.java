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

	public static final String KEY_SPACE = " ";
	public static final String CONTINUATION = " ...";
	
	public static String getOwnership(String username){
		
		String last = username.substring(username.length()-1);
		
		if("s".equals(last) || "S".equals(last))
			return username + "'";
		else
			return username + "'s";
		
	}
	
	public static String shorten(String string, int length, String continuation){
		
		if(string.length()<= length) return string;
		
		int cl = continuation.length();
		int max = length - cl - 1;
		String substring = string.substring(0, max);
		
		if(substring.contains(KEY_SPACE)){
			String composite = "";
			String[] tokens = string.split(KEY_SPACE);
			String next = "";
			for(int i=0;i<tokens.length;i++){
				if(!composite.equals("")) composite += " ";
				next = tokens[i];
				if(composite.length() + next.length() > max)
					break;
				
				composite += next;
			}
			
			return composite.trim() + continuation;
		} else {
			return string.substring(0, max) + continuation;
		}
		
	}
	
	public static String shorten(String string, int length){
		return shorten(string, length, CONTINUATION);
	}
	
}
