/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.activity;

import org.hummer.kickstalker.R;
import org.hummer.kickstalker.fragment.ProjectListFragment;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;

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
		ft.add(R.id.appContent, fragment, "projectListFragment");
		
		ft.commit();
		
	}

	
	
}
