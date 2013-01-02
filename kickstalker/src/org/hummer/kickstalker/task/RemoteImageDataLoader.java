/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.task;

import java.io.IOException;

import org.hummer.kickstalker.cache.ImageCache;
import org.hummer.kickstalker.client.KickstarterClient;
import org.hummer.kickstalker.task.i.TaskCallbackI;
import org.hummer.kickstalker.util.MediaUtil;

import android.content.Context;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class RemoteImageDataLoader extends
		AbstractTask<String, Void, byte[]> {

	private static final String TASKNAME = "REMOTEIMAGEDATALOADER";
	private Context context;
	private ImageCache cache;
	private String ref="";

	/**
	 * @param name
	 * @param client
	 * @param callback
	 */
	public RemoteImageDataLoader(KickstarterClient client,
			TaskCallbackI callback, ImageCache cache, Context context) {
		super(TASKNAME, client, callback);
		this.cache = cache;
		this.context = context;
	}

	/* (non-Javadoc)
	 * @see android.os.AsyncTask#doInBackground(Params[])
	 */
	@Override
	protected byte[] doInBackground(String... params) {
		
		ref = params[0];
		
		try {
			return MediaUtil.extractImage(context, params[0], cache);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return new byte[]{};
	}

	@Override
	protected void onPostExecute(byte[] result) {
		callback.onTaskFinished(this, result);
	}
	
	public String getRef(){
		return ref;
	}

}
