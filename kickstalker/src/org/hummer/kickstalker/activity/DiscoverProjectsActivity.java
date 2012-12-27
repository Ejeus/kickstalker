/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.activity;

import org.hummer.kickstalker.R;
import org.hummer.kickstalker.fragment.ProjectListFragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
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
public class DiscoverProjectsActivity extends BaseActivity {

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discover);
		
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		Fragment fragment = new ProjectListFragment();
		Bundle args = new Bundle();
		args.putString(ProjectListFragment.KEY_TYPE, 
				ProjectListFragment.TYPE_DISCOVER);
		
		fragment.setArguments(args);
		ft.add(R.id.appContent, fragment, "projectListFragment");
		
		ft.commit();		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_options_discover, menu);
		return true;
		
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		Intent i;
		switch(item.getItemId()){
		
		case R.id.ac_backed_projects:
			i = new Intent(this, BackedProjectsActivity.class);
			startActivity(i);
			return true;
		case R.id.ac_config:
			i = new Intent(this, ConfigurationActivity.class);
			startActivity(i);
			return true;
		default:
			return false;
		}
	}
	
	

	
	
}
