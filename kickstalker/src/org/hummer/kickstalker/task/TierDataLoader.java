/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hummer.kickstalker.client.KickstarterClient;
import org.hummer.kickstalker.data.Tier;
import org.hummer.kickstalker.task.i.TaskCallbackI;

import android.content.Context;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class TierDataLoader extends AbstractTask<Void, Void, List<Tier>>{
	
	public static final String TASKNAME = "TIERDATALOADER";
	private Context context;
	private String ref;

	public TierDataLoader(Context context, KickstarterClient client, 
			TaskCallbackI callback, String ref){
		super(TASKNAME, client, callback);
		this.context = context;
		this.ref = ref;
	}
	
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected List<Tier> doInBackground(Void... params) {
		try {
			return client.getTiersFor(context, ref);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new ArrayList<Tier>();
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(List<Tier> result) {
		callback.onTaskFinished(this, result);
	}
	
	
	
}