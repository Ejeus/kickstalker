/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.activity;

import org.hummer.kickstalker.AppController;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class BaseActivity extends Activity {

	public static final String RETURN_TO = "NAV_RETURNTO";
	protected AppController appC;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		appC = AppController.getInstance();
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
	}
	
	public AppController getAppController(){
		return appC;
	}
	
}
