/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.activity;

import org.hummer.kickstalker.R;
import org.hummer.kickstalker.client.KickstarterClient;
import org.hummer.kickstalker.fragment.KickstarterListFragment;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

/**
 * A list view for showing Kickstarter projects.
 * 
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class ProjectListActivity extends BaseActivity {

	private KickstarterListFragment listFragment;
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
		FragmentManager fmgr = getFragmentManager();
		setContentView(R.layout.activity_main);
		listFragment = new KickstarterListFragment();
		
		Intent i = getIntent();
		Bundle args;
		
		if(i.getExtras()!=null){
			args = i.getExtras();
		} else {
			args = new Bundle();
		}
		
		listFragment.setArguments(args);
		((ViewGroup)findViewById(R.id.appContent)).removeAllViews();
		fmgr.beginTransaction().
			add(R.id.appContent, listFragment, KickstarterListFragment.TAG).commit();
		
	}
	
}
