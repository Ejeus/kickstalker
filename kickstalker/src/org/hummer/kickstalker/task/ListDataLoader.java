/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hummer.kickstalker.client.KickstarterClient;
import org.hummer.kickstalker.data.Reference;
import org.hummer.kickstalker.task.i.TaskCallbackI;

/**
 * The internal data loader to fill the data into the list asynchronously.
 * 
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class ListDataLoader extends AbstractTask<Void, Void, List<Reference>>{

	public static final String KEY_USERNAME = "USERNAME";
	public static final String TYPE_DISCOVER = "DISCOVER";
	public static final String TYPE_BACKED = "BACKED";
	public static final String TASKNAME = "LISTDATALOADER";
	
	String type;
	String username;
	
	/**
	 * @param name
	 * @param client
	 * @param callback
	 */
	public ListDataLoader(KickstarterClient client,
			TaskCallbackI callback, String type, String username) {
		super(TASKNAME, client, callback);
		this.type = type;
		this.username = username;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected List<Reference> doInBackground(Void... params) {
		
		callback.onTaskStarted(this);;
		try {
			if(type.equals(TYPE_BACKED)){
				return client.getBackedProjects(username);
			}else{
				return client.getDiscoverProjects();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new ArrayList<Reference>();
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(List<Reference> result) {

		callback.onTaskFinished(this, result);
		
	}
	
}