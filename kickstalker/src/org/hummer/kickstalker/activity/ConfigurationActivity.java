/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.activity;

import org.hummer.kickstalker.R;
import org.hummer.kickstalker.client.KickstarterClient;
import org.hummer.kickstalker.data.Configuration;
import org.hummer.kickstalker.util.ViewUtil;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

/**
 * General app configuration activity for settings.
 * 
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class ConfigurationActivity extends BaseActivity {

	private Configuration config;
	private TextView username;
	private KickstarterClient client;
	
	@Override
	public KickstarterClient getClient(){
		return client;
	}
	
	/* (non-Javadoc)
	 * @see org.hummer.kickstalker.activity.BaseActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		client = new KickstarterClient(this);
		setContentView(R.layout.activity_configuration);
		config = appC.getConfig(this);
		
		username = ViewUtil.findAndSetText(this, 
				R.id.fieldUsername, config.getUsername());
		
	}



	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_config, menu);
		return true;
		
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch(item.getItemId()){
		case R.id.ac_save:
			config.setUsername(username.getText().toString());
			appC.persistConfig(this);
			finish();
			return true;
		case R.id.ac_cancel:
			finish();
			return true;
		default: 
			return false;
		}
		
	}
	
	

	
	
}
