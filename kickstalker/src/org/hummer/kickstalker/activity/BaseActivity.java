/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.activity;

import org.hummer.kickstalker.AppController;
import org.hummer.kickstalker.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean returnVal = super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_app, menu);
		return returnVal;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean returnVal = super.onOptionsItemSelected(item);
		
		if(!returnVal){
			switch(item.getItemId()){
			case R.id.ac_config:
				Intent i = new Intent(this, ConfigurationActivity.class);
				startActivity(i);
				return true;
			}
		}
		
		return returnVal;
	}
	
}
