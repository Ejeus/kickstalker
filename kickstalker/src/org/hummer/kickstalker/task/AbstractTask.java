/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.task;

import org.hummer.kickstalker.client.KickstarterClient;
import org.hummer.kickstalker.task.i.TaskCallbackI;

import android.os.AsyncTask;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public abstract class AbstractTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {

	private String name;
	protected KickstarterClient client;
	protected TaskCallbackI callback;
	
	public AbstractTask(String name, KickstarterClient client, TaskCallbackI callback){
		this.name = name;
		this.client = client;
		this.callback = callback;
	}
	
	public String getName(){
		return name;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
		
		callback.onTaskCancelled(this);
	}
	
	
	
}
