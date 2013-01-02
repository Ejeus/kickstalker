/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.task.i;

import org.hummer.kickstalker.task.AbstractTask;

/**
 * A generic callback for asynchronous tasks.
 * 
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public interface TaskCallbackI {

	/**
	 * Fired when task starts working.
	 * 
	 * @param task, AbstractTask. The task that just fired the event.
	 */
	public void onTaskStarted(AbstractTask<?, ?, ?> task);
	
	/**
	 * Fired after a task has finished its work.
	 * 
	 * @param task, AbstractTask. The task that just finished.
	 * @param result, Object. A result the task is supposed to return.
	 */
	public void onTaskFinished(AbstractTask<?, ?, ?> task, Object result);
	
	/**
	 * Fired if a task was cancelled.
	 * 
	 * @param task, AbstractTask. The task that was cancelled.
	 */
	public void onTaskCancelled(AbstractTask<?, ?, ?> task);
	
}
