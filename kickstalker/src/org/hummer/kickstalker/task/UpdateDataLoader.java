/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hummer.kickstalker.client.KickstarterClient;
import org.hummer.kickstalker.data.Update;
import org.hummer.kickstalker.task.i.TaskCallbackI;

import android.content.Context;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class UpdateDataLoader extends AbstractTask<Void, Void, List<Update>>{
	
	public static final String TASKNAME = "UPDATEDATALOADER";
	private Context context;
	private String ref;

	public UpdateDataLoader(Context context, KickstarterClient client, 
			TaskCallbackI callback, String ref){
		super(TASKNAME, client, callback);
		this.context = context;
		this.ref = ref;
	}
	
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected List<Update> doInBackground(Void... params) {
		
		callback.onTaskStarted(this);
		try {
			return client.getUpdatesFor(context, ref);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new ArrayList<Update>();
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(List<Update> result) {
		callback.onTaskFinished(this, result);
	}
	
	
	
}