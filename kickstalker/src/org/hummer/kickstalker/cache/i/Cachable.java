/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.cache.i;

import java.io.Serializable;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public interface Cachable extends Serializable {

	public long getCacheStamp();
	
}
