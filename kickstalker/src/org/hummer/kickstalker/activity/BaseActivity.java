/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.activity;

import org.hummer.kickstalker.AppController;
import org.hummer.kickstalker.client.KickstarterClient;

import android.app.Activity;
import android.os.Bundle;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class BaseActivity extends Activity {

	protected AppController appC;
	protected KickstarterClient client;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		appC = AppController.getInstance();
		client = appC.getClient();
		
	}
	
	public AppController getAppController(){
		return appC;
	}
	
	public KickstarterClient getClient(){
		return client;
	}
	
}
