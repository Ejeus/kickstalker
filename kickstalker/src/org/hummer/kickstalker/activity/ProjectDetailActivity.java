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
import android.view.MenuItem;
import android.view.ViewGroup;

/**
 * A detailed view for projects on Kickstarter.
 * 
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class ProjectDetailActivity extends BaseActivity implements TaskCallbackI {

	public static final String TAG = "PRJDTLAC";
	public static final String KEY_PRJREF = "PRJREF";
	private KickstarterClient client;
	private KickstarterDetailFragment detailFragment;
	private AbstractTask<?,?,?> currentTask;
//	private TouchHandler touchHandler;
	
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
//		touchHandler = new TouchHandler();
		setContentView(R.layout.activity_main);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent i = getIntent();
		if(i!=null && i.hasExtra(KEY_PRJREF)){
			Reference ref = (Reference) i.getExtras().getSerializable(KEY_PRJREF);
			new DetailDataLoader(this, client, this).execute(ref);
		}
		
	}
	
	/* (non-Javadoc)
	 * @see org.hummer.kickstalker.activity.BaseActivity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case android.R.id.home:
			home();
			return false;
					
		}
		
		return super.onOptionsItemSelected(item);
	}

	/* (non-Javadoc)
	 * @see org.hummer.kickstalker.activity.BaseActivity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		if(phase==Phase.BUSY)
			currentTask.cancel(true);
		
		phase=Phase.STOPPED;
	}



//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//
//		switch(event.getAction()){
//		case MotionEvent.ACTION_DOWN:
//			touchHandler.clear(event.getX(), event.getY());
//			return true;
//		case MotionEvent.ACTION_MOVE:
//			touchHandler.setCurrentX(event.getX());
//			touchHandler.setCurrentY(event.getY());
//			return true;
//		case MotionEvent.ACTION_UP:
//			touchHandler.process();
//			return true;
//		}
//		
//		return super.onTouchEvent(event);
//	}

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
	
//	private class TouchHandler{
//
//		float currentX, currentY;
//		float movedX, movedY;
//				
//		
//		public void setCurrentX(float x){
//			movedX = x - currentX;
//			currentX = x;
//		}
//		
//		public void setCurrentY(float y){
//			movedY = y - currentY;
//			currentY = y;
//		}
//		
//		public void process(){
//			Log.i(TAG, "Processing touch event ...");
//			TabHost tabHost = detailFragment.getTabHost();
//			int current = tabHost.getCurrentTab();
//			int ubound = tabHost.getTabWidget().getTabCount() - 1;
//			if(movedX < 0 && current > 0){
//				tabHost.setCurrentTab(--current);
//			}else if(movedX > 0 && current < ubound){
//				tabHost.setCurrentTab(++current);
//			}
//		}
//		
//		public void clear(float x, float y){
//			currentX = x;
//			currentY= y;
//			movedX = 0;
//			movedY = 0;
//		}
//	}
//	
}
