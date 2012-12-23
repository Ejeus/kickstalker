/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.hummer.kickstalker.AppController;
import org.hummer.kickstalker.R;
import org.hummer.kickstalker.activity.BaseActivity;
import org.hummer.kickstalker.client.KickstarterClient;
import org.hummer.kickstalker.data.Project;
import org.hummer.kickstalker.data.Reference;
import org.hummer.kickstalker.view.ProjectCardListView;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * A generic fragment that can be used to present a list of projects.
 * 
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class ProjectListFragment extends Fragment {

	public static final String TAG = "FR-PRJLIST";
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);		
		new DataLoader().execute();
		
	}
	
	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_list_general, container, false);
	}

	/**
	 * @param prjRefs, List<References> - The project references to load the list
	 * 	view from.
	 */
	private void refreshContent(List<Project> prjRefs){
		
		Activity current = getActivity();
		LinearLayout main = 
				(LinearLayout) getActivity().findViewById(R.id.mainContent);
		ScrollView container = new ScrollView(current);
		View tiledView = new ProjectCardListView(current, prjRefs);
		container.addView(tiledView);
		
		main.removeAllViews();
		main.addView(container);
	}


	/**
	 * The internal data loader to fill the data into the list asynchronously.
	 * 
	 * @author gernot.hummer
	 *
	 * @version 1.0
	 *
	 */
	private class DataLoader extends AsyncTask<Void, Void, List<Project>>{

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected List<Project> doInBackground(Void... params) {
			
			KickstarterClient client = AppController.getInstance().getClient();
			try {
				List<Reference> refs = client.getDiscoverProjects();
				return client.getProjectsFromRef((BaseActivity) getActivity(), refs);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return new ArrayList<Project>();
		}

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		@Override
		protected void onPostExecute(List<Project> result) {

			refreshContent(result);
			
		}
		
	}
	
}
