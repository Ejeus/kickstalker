/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.activity;

import org.hummer.kickstalker.R;
import org.hummer.kickstalker.client.KickstarterClient;
import org.hummer.kickstalker.data.Project;
import org.hummer.kickstalker.data.Reference;
import org.hummer.kickstalker.fragment.KickstarterDetailFragment;
import org.hummer.kickstalker.fragment.KickstarterListFragment;
import org.hummer.kickstalker.task.AbstractTask;
import org.hummer.kickstalker.task.DetailDataLoader;
import org.hummer.kickstalker.task.i.TaskCallbackI;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.ViewGroup;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class ProjectDetailActivity extends BaseActivity implements TaskCallbackI {

	public static final String TAG = "PRJDTLAC";
	public static final String KEY_PRJREF = "PRJREF";
	private KickstarterDetailFragment detailFragment;
	private AbstractTask<?,?,?> currentTask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Intent i = getIntent();
		if(i!=null && i.hasExtra(KEY_PRJREF)){
			Reference ref = (Reference) i.getExtras().getSerializable(KEY_PRJREF);
			new DetailDataLoader(this, new KickstarterClient(this), this).execute(ref);
		}
		
	}
	
	

	@Override
	protected void onStop() {
		super.onStop();
		if(phase==Phase.BUSY)
			currentTask.cancel(true);
		
		phase=Phase.STOPPED;
	}



	/* (non-Javadoc)
	 * @see org.hummer.kickstalker.task.i.TaskCallbackI#onTaskStarted(org.hummer.kickstalker.task.AbstractTask)
	 */
	@Override
	public void onTaskStarted(AbstractTask<?, ?, ?> task) {
		currentTask = task;
		phase = Phase.BUSY;
	}

	/* (non-Javadoc)
	 * @see org.hummer.kickstalker.task.i.TaskCallbackI#onTaskFinished(org.hummer.kickstalker.task.AbstractTask, java.lang.Object)
	 */
	@Override
	public void onTaskFinished(AbstractTask<?, ?, ?> task, Object result) {
		
		if(phase==Phase.STOPPED) return;
		FragmentManager fmgr = getFragmentManager();
		setContentView(R.layout.activity_main);
		detailFragment = new KickstarterDetailFragment();
		
		((ViewGroup)findViewById(R.id.appContent)).removeAllViews();
		fmgr.beginTransaction().
			add(R.id.appContent, detailFragment, 
					KickstarterListFragment.TAG).commit();
		
		detailFragment.setData((Project) result);
		phase = Phase.IDLE;
		currentTask = null;
		
	}

	/* (non-Javadoc)
	 * @see org.hummer.kickstalker.task.i.TaskCallbackI#onTaskCancelled(org.hummer.kickstalker.task.AbstractTask)
	 */
	@Override
	public void onTaskCancelled(AbstractTask<?, ?, ?> task) {
		phase = Phase.IDLE;
		currentTask = null;
	}
	
}
