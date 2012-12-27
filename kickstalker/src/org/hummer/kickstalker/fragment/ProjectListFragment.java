/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hummer.kickstalker.R;
import org.hummer.kickstalker.activity.BaseActivity;
import org.hummer.kickstalker.adapter.ProjectCardAdapter;
import org.hummer.kickstalker.client.KickstarterClient;
import org.hummer.kickstalker.data.Project;
import org.hummer.kickstalker.data.Reference;
import org.hummer.kickstalker.view.ProjectCardView;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * A generic fragment that can be used to present a list of projects.
 * 
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class ProjectListFragment extends Fragment implements OnItemClickListener {

	public static final String TAG = "PRJLSTFR";
	public static final String KEY_TITLE = "TITLE";
	public static final String KEY_USERNAME = "USERNAME";
	public static final String KEY_TYPE = "TYPE";
	public static final String TYPE_DISCOVER = "DISCOVER";
	public static final String TYPE_BACKED = "BACKED";
	
	static final int PHASE_IDLE = 0;
	static final int PHASE_BUSY = 1;
	private ProjectListFragment self;
	private static final String KEY_PROJECT_LIST = "PROJECTLIST";
	KickstarterClient client;
	private List<Reference> projects;
	private int phase;
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		self = this;
		super.onCreate(savedInstanceState);	
		client = new KickstarterClient(getActivity());
		new ListDataLoader().execute();
		
	}
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_list_general, container, false);
		
		Bundle args = getArguments();
		if(args!=null && args.containsKey(KEY_TITLE)){
			TextView title = (TextView) view.findViewById(R.id.listTitle);
			title.setText(args.getString(KEY_TITLE));
		}
		
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		
		super.onSaveInstanceState(outState);
		outState.putSerializable(KEY_PROJECT_LIST, (ArrayList<Reference>)projects);
		try {
			client.persistCache(getActivity());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	

	@Override
	public void onStop() {

		super.onStop();
		try {
			client.persistCache(getActivity());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * @param prjRefs, List<References> - The project references to load the list
	 * 	view from.
	 */
	private void refreshContent(List<Reference> projects){
		
		this.projects = projects;
		Activity current = getActivity();
		LinearLayout main = 
				(LinearLayout) getActivity().findViewById(R.id.mainContent);
		
		GridView grid = new GridView(current);
		grid.setNumColumns(2);
		ProjectCardAdapter adapter = new ProjectCardAdapter();
		adapter.setData(current, projects);
		grid.setAdapter(adapter);
		grid.setOnItemClickListener(this);
		
		main.removeAllViews();
		main.addView(grid);
		
	}
	
	/* (non-Javadoc)
	 * @see android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget.AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View item, int position, long row) {
		
		if(phase!=PHASE_IDLE) return;
		new DetailDataLoader().execute(
				((ProjectCardView)item).getProjectReference());
		
	}
	
	private void toggleDetailFragment(Project project,
			FragmentTransaction ft){
		
		
		Fragment newFragment = getFragmentManager().findFragmentByTag(
				ProjectDetailFragment.TAG);
				
		if(newFragment==null){ 
			newFragment = new ProjectDetailFragment();
			ft.add(R.id.appContent, newFragment, ProjectDetailFragment.TAG);
		}
		
		ft.show(newFragment);
		
		
		Bundle args = new Bundle();
		args.putSerializable(
				ProjectDetailFragment.KEY_PROJECT, project);
		args.putString(BaseActivity.RETURN_TO, getTag());
		newFragment.setArguments(args);
		
		
	}

	/**
	 * The internal data loader to fill the data into the list asynchronously.
	 * 
	 * @author gernot.hummer
	 *
	 * @version 1.0
	 *
	 */
	private class ListDataLoader extends AsyncTask<Void, Void, List<Reference>>{

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected List<Reference> doInBackground(Void... params) {
			
			phase = PHASE_BUSY;
			Bundle args = getArguments();
			String type = args.getString(KEY_TYPE);
			
			try {
				if(type.equals(TYPE_BACKED)){
					return client.getBackedProjects(args.getString(KEY_USERNAME));
				}else{
					return client.getDiscoverProjects();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return new ArrayList<Reference>();
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(List<Reference> result) {

			refreshContent(result);
			phase = PHASE_IDLE;
			
		}
		
	}
	
	/**
	 * Internal data loader for project details.
	 * 
	 * @author gernot.hummer
	 *
	 * @version 1.0
	 *
	 */
	private class DetailDataLoader extends AsyncTask<Reference, Void, Project>{

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected Project doInBackground(Reference... params) {
			phase = PHASE_BUSY;
			try {
				return client.getProjectFromRef(getActivity(), params[0]);
			} catch (IOException e) {
				e.printStackTrace();
				return new Project(params[0].getRef());
			}
		}

		@Override
		protected void onPostExecute(Project result) {
			
			FragmentManager mgr = getFragmentManager();
			FragmentTransaction ft = mgr.beginTransaction();
		
			ft.hide(self);
			toggleDetailFragment(result, ft);
			
			
			ft.addToBackStack(null);
			ft.setTransitionStyle(R.anim.incoming);
			
			phase = PHASE_IDLE;
			ft.commit();
			
		}
		
		
		
	}
	
}
