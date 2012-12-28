/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.fragment;

import java.util.List;

import org.hummer.kickstalker.AppController;
import org.hummer.kickstalker.R;
import org.hummer.kickstalker.activity.BaseActivity.Phase;
import org.hummer.kickstalker.activity.ProjectDetailActivity;
import org.hummer.kickstalker.activity.ProjectListActivity;
import org.hummer.kickstalker.adapter.ProjectCardAdapter;
import org.hummer.kickstalker.client.KickstarterClient;
import org.hummer.kickstalker.data.Reference;
import org.hummer.kickstalker.task.AbstractTask;
import org.hummer.kickstalker.task.ListDataLoader;
import org.hummer.kickstalker.task.i.TaskCallbackI;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class KickstarterListFragment extends Fragment implements TaskCallbackI {

	public static final String TAG = "KICKSTARTERLISTFRAGMENT";
	public static final String TYPE_DISCOVER = "DISCOVER";
	public static final String TYPE_BACKED = "BACKED";
	
	public static final String KEY_TYPE = "TYPE";
	public static final String KEY_USERNAME = "USERNAME";
	public static final String KEY_TITLE = "TITLE";
	
	private Phase phase;
	private AbstractTask<?, ?, ?> currentTask;
	private Context context;
	private KickstarterClient client;
	private GridView listView;
	private TextView titleView;
	private String type;
	private String username;
	private String title;
	private boolean listAttached;
	private List<Reference> data;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		listView = new GridView(getActivity());
		listView.setNumColumns(2);
		listView.setGravity(Gravity.TOP);
		listAttached = false;
		
		View view = inflater.inflate(R.layout.fragment_list_general, container, false);
		titleView = (TextView) view.findViewById(R.id.listTitle);
		titleView.setText(title);
		
		return view;
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		context = getActivity();
		super.onCreate(savedInstanceState);
		
		setHasOptionsMenu(true);
		client = new KickstarterClient(context);
		Bundle args = getArguments();
		if(args!=null){
			
			if(args.containsKey(KEY_TYPE)){
				type = args.getString(KEY_TYPE);
				if(args.containsKey(KEY_USERNAME)){
					username = args.getString(KEY_USERNAME);
				} else {
					username = AppController.getInstance().
							getConfig(context).getUsername();
				}
			}else{
				type = TYPE_DISCOVER;
			}
			
			if(args.containsKey(KEY_TITLE))
				title = args.getString(KEY_TITLE);
			
		} else type = TYPE_DISCOVER;
		
		if(title==null || title.equals(""))
			title = getResources().getString(
					R.string.fragment_caption_discover);
		
		phase = Phase.IDLE;
		dispatchRequest(type, username);
		
	}
	
	/**
	 * Dispatches a request to KickstarterClient for project retrieval based
	 * on the handed in parameters.
	 * 
	 * @param type, String. The type of request to dispatch to the client.
	 * @param username, String. The username to load backed projects for.
	 * 
	 * The latter parameter only needs to be set, if the handed in type is
	 * <code>TYPE_BACKED</code>.
	 */
	public void dispatchRequest(String type, String username){
		new ListDataLoader(client, this, type, username).execute();
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.options_kickstarterlistfragment, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(phase==Phase.IDLE){
			Intent i;
			switch(item.getItemId()){
			
			case R.id.ac_projects_backed:
				String username = AppController.getInstance().
					getConfig(context).getUsername();
				if(username.equals(this.username)) return true;
				i = new Intent(context, ProjectListActivity.class);
				i.putExtra(KEY_TYPE, TYPE_BACKED);
				i.putExtra(KEY_TITLE, "My " + 
						getResources().getString(R.string.ac_backed_projects));
				startActivity(i);
				return true;
			case R.id.ac_projects_discover:
				if(type.equals(TYPE_DISCOVER)) return true;
				i = new Intent(context, ProjectListActivity.class);
				startActivity(i);
				return true;
			}
		}
		
		return false;
	}
	
	

	@Override
	public void onStop() {

		super.onStop();
		
		if(phase==Phase.BUSY)
			currentTask.cancel(true);
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
	@SuppressWarnings("unchecked")
	@Override
	public void onTaskFinished(AbstractTask<?, ?, ?> task, Object result) {
		
		if(task.getName().equals(ListDataLoader.TASKNAME)){
			ProjectCardAdapter adapter = new ProjectCardAdapter();
			data = (List<Reference>) result;
			adapter.setData(context, data);
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener(){

				@Override
				public void onItemClick(AdapterView<?> parent, View item,
						int pos, long id) {
					
					Intent i = new Intent(context, ProjectDetailActivity.class);
					i.putExtra(ProjectDetailActivity.KEY_PRJREF, data.get(pos));
					startActivity(i);
				}
				
			});
			
			if(!listAttached){
				LinearLayout content = 
					(LinearLayout) getView().findViewById(R.id.listContent);
				
				content.removeAllViews();
				content.addView(listView);
				content.invalidate();
			}
		}
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
