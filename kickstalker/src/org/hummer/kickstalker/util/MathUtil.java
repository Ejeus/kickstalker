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
public class MathUtil {

	/**
	 * @param i
	 * @param j
	 * @param k
	 * @return
	 */
	public static int constrain(int val, int min, int max) {
		
		if(val<min)
			return min;
		else if(val>max)
			return max;
		
		return val;
		
	}

}
