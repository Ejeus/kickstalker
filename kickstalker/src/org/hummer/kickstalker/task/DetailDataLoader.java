/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.task;

import java.io.IOException;

import org.hummer.kickstalker.client.KickstarterClient;
import org.hummer.kickstalker.data.Project;
import org.hummer.kickstalker.data.Reference;
import org.hummer.kickstalker.task.i.TaskCallbackI;

import android.content.Context;

/**
 * Internal data loader for project details.
 * 
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class DetailDataLoader extends AbstractTask<Reference, Void, Project>{

	public static final String TASKNAME = "DETAILDATALOADER";
	private Context context;
	
	/**
	 * @param name
	 * @param client
	 * @param callback
	 */
	public DetailDataLoader(Context context, KickstarterClient client,
			TaskCallbackI callback) {
		super(TASKNAME, client, callback);
		this.context = context;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected Project doInBackground(Reference... params) {
		
		callback.onTaskStarted(this);
		try {
			return client.getProjectFromRef(context, params[0]);
		} catch (IOException e) {
			e.printStackTrace();
			return new Project(params[0].getRef());
		}
	}

	@Override
	protected void onPostExecute(Project result) {
		
		callback.onTaskFinished(this, result);
		
	}
	
	
	
}