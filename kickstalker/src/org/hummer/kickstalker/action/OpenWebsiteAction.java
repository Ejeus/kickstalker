/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.action;

import org.hummer.kickstalker.client.KickstarterClient;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class OpenWebsiteAction implements OnClickListener {
	
	private Context context;
	private String ref;

	public OpenWebsiteAction(Context context, String ref){
		this.context = context;
		this.ref = KickstarterClient.BASE_URL + "/" + ref;
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setData(Uri.parse(ref));
		context.startActivity(i);
		
	}

}
