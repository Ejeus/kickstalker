/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.task.i;

import org.hummer.kickstalker.task.AbstractTask;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public interface TaskCallbackI {

	public void onTaskFinished(AbstractTask<?, ?, ?> task, Object result);
	
}
