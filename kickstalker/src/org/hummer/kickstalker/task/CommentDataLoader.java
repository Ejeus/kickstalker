/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hummer.kickstalker.client.KickstarterClient;
import org.hummer.kickstalker.data.Comment;
import org.hummer.kickstalker.task.i.TaskCallbackI;

import android.content.Context;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class CommentDataLoader extends AbstractTask<Void, Void, List<Comment>>{
	
	public static final String TASKNAME = "COMMENTDATALOADER";
	private Context context;
	private String ref;

	public CommentDataLoader(Context context, KickstarterClient client, 
			TaskCallbackI callback, String ref){
		super(TASKNAME, client, callback);
		this.context = context;
		this.ref = ref;
	}
	
	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected List<Comment> doInBackground(Void... params) {
		try {
			return client.getCommentsFor(context, ref);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new ArrayList<Comment>();
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
	 */
	@Override
	protected void onPostExecute(List<Comment> result) {
		callback.onTaskFinished(this, result);
	}
	
	
	
}