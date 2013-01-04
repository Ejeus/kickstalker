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
import org.hummer.kickstalker.fragment.KickstarterListFragment;
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
	public static final String TASKNAME = "LISTDATALOADER";
	
	String type;
	String filterParam;
	
	/**
	 * @param name
	 * @param client
	 * @param callback
	 */
	public ListDataLoader(KickstarterClient client,
			TaskCallbackI callback, String type, String filterParam) {
		super(TASKNAME, client, callback);
		this.type = type;
		this.filterParam = filterParam;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected List<Reference> doInBackground(Void... params) {
		
		callback.onTaskStarted(this);
		if(filterParam==null) filterParam = "";
		
		try {
			if(type.equals(KickstarterListFragment.TYPE_SEARCH)){
				return client.getProjectsFor(filterParam);
			} else if(type.equals(KickstarterListFragment.TYPE_BACKED)){
				return client.getBackedProjects(filterParam);
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