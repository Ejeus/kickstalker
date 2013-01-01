/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.fragment;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import org.hummer.kickstalker.R;
import org.hummer.kickstalker.activity.BaseActivity;
import org.hummer.kickstalker.activity.BaseActivity.Phase;
import org.hummer.kickstalker.activity.ProjectListActivity;
import org.hummer.kickstalker.adapter.CommentAdapter;
import org.hummer.kickstalker.adapter.TierAdapter;
import org.hummer.kickstalker.adapter.UpdateAdapter;
import org.hummer.kickstalker.builder.DetailFragmentBuilder;
import org.hummer.kickstalker.client.KickstarterClient;
import org.hummer.kickstalker.data.BookmarkBundle;
import org.hummer.kickstalker.data.Comment;
import org.hummer.kickstalker.data.Project;
import org.hummer.kickstalker.data.Reference;
import org.hummer.kickstalker.data.Tier;
import org.hummer.kickstalker.data.Update;
import org.hummer.kickstalker.factory.BookmarkFactory;
import org.hummer.kickstalker.task.AbstractTask;
import org.hummer.kickstalker.task.CommentDataLoader;
import org.hummer.kickstalker.task.TierDataLoader;
import org.hummer.kickstalker.task.UpdateDataLoader;
import org.hummer.kickstalker.task.i.TaskCallbackI;
import org.hummer.kickstalker.util.MediaUtil;
import org.hummer.kickstalker.util.StringUtil;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class KickstarterDetailFragment extends Fragment implements 
	OnTabChangeListener, TaskCallbackI {

	public static final String TAG = "KICKSTARTERDETAILFRAGMENT";
	
	private Context context;
	private KickstarterClient client;
	private Project project;
	private ImageView imageView;
	private int screenWidth;
	private Phase phase;
	AbstractTask<?,?,?> currentTask;
	private BookmarkBundle projectBookmarks;
	private ArrayAdapter<Reference> starredProjects;
	private MenuItem ac_bookmark;
	private boolean isBookmark = false;
	int ac_bookmark_non;
	int ac_bookmark_on;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		context = getActivity();
		BaseActivity activity = ((BaseActivity)context);
		projectBookmarks = activity.getProjectBookmarks();
		starredProjects = activity.getStarredProjects();
		setHasOptionsMenu(true);
		
		super.onCreate(savedInstanceState);
		client = new KickstarterClient(context);
		
		Point p = new Point();
		getActivity().getWindowManager().getDefaultDisplay().getSize(p);
		screenWidth = p.x;
		ac_bookmark_non = R.drawable.ac_fav_non;
		ac_bookmark_on = R.drawable.ac_fav_on;
		
		phase = Phase.IDLE;
		
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.options_kickstarterdetailfragment, menu);
		
		ac_bookmark = menu.findItem(R.id.ac_bookmark);
		initBookmarkState();
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(
				R.layout.fragment_detail_project, container, false);
		
		imageView = (ImageView) view.findViewById(R.id.projectImage);
		DetailFragmentBuilder.initialize(getActivity(), view);
		
		((TabHost)view.findViewById(android.R.id.tabhost)).
			setOnTabChangedListener(this);
		
		refresh(view, project);
		return view;
		
	}
	
	public void setData(Project project){
		
		if(project==null || !project.equals(this.project))
			refresh(getView(), project);
		
		this.project = project;
	}
	
	private void refresh(View view, final Project project){
		
		final Context context = getActivity();
		int[] allIds = {
				R.id.projectTierContent,
				R.id.projectUpdateContent,
				R.id.projectUpdateContent
		};
		
		if(view!=null && project!=null){
			DetailFragmentBuilder.buildDetails(view, imageView, screenWidth, project);
			imageView.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					MediaUtil.streamVideo(context, project.getVideoReference().getRef());				
				}
				
			});
			
			view.findViewById(R.id.fieldTitle).setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Intent i = new Intent(Intent.ACTION_VIEW);
					i.setData(Uri.parse(
							KickstarterClient.BASE_URL + "/" + project.getRef()));
					startActivity(i);
				}
				
			});
			
			view.findViewById(R.id.fieldCreator).setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					Reference user = project.getOwner();
					String title = StringUtil.getOwnership(user.getLabel()) + " " +
							getResources().getString(R.string.ac_backed_projects);
					
					Intent i = new Intent(context, ProjectListActivity.class);
					i.putExtra(KickstarterListFragment.KEY_TITLE, title);
					i.putExtra(KickstarterListFragment.KEY_TYPE, KickstarterListFragment.TYPE_BACKED);
					i.putExtra(KickstarterListFragment.KEY_USERNAME, user.getRef());
					startActivity(i);
				}
				
			});
			
			initBookmarkState();
			
		} else if(view!=null) {
			DetailFragmentBuilder.clearAll(context, view, allIds);
		}
		
	}
	
	

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		if(phase==Phase.BUSY) return false;
		
		switch(item.getItemId()){
		case R.id.ac_bookmark:
			toggleBookmark();
			return true;
		case R.id.ac_share:
			share();
			return true;
		}
		
		return false;
	}

	private void initBookmarkState(){
		if(ac_bookmark==null || project==null) return;
		if(projectBookmarks.contains(project.asReference())){
			ac_bookmark.setIcon(ac_bookmark_on);
			isBookmark = true;
		} else {
			ac_bookmark.setIcon(ac_bookmark_non);
			isBookmark = false;
		}
	}
	
	private void toggleBookmark(){
		Reference ref = new Reference(project.getRef(), project.getTitle());
		
		if(isBookmark){
			int i = projectBookmarks.indexOf(ref);
			if(i>=0){ 
				starredProjects.remove(projectBookmarks.get(i));
				starredProjects.notifyDataSetChanged();
			}
			isBookmark = false;
			ac_bookmark.setIcon(ac_bookmark_non);
		} else {
			starredProjects.add(ref);
			Collections.sort(projectBookmarks);
			starredProjects.notifyDataSetChanged();
			isBookmark = true;
			ac_bookmark.setIcon(ac_bookmark_on);
		}
		
		try {
			BookmarkFactory.store(context, projectBookmarks);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void share(){
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_SUBJECT, project.getTitle());
		i.putExtra(Intent.EXTRA_TEXT, KickstarterClient.BASE_URL + "/" + 
				project.getRef());
		startActivity(i);
	}
	
	@Override
	public void onStop() {
		super.onStop();
		if(phase==Phase.BUSY)
			currentTask.cancel(true);
		
		phase = Phase.STOPPED;
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
		
		if(phase==Phase.STOPPED) return;
		View view = getView();
		if(task.getName().equals(TierDataLoader.TASKNAME)){
			TierAdapter adapter = new TierAdapter();
			adapter.setData(context, (List<Tier>) result);
			DetailFragmentBuilder.buildListAndAttach(context, 
					(ViewGroup) view.findViewById(R.id.projectTierContent), adapter);
		}else if(task.getName().equals(UpdateDataLoader.TASKNAME)){
			UpdateAdapter adapter = new UpdateAdapter();
			adapter.setData(context, (List<Update>) result);
			DetailFragmentBuilder.buildListAndAttach(context, 
					(ViewGroup) view.findViewById(R.id.projectUpdateContent), adapter);
		}else if(task.getName().equals(CommentDataLoader.TASKNAME)){
			CommentAdapter adapter = new CommentAdapter();
			adapter.setData(context, (List<Comment>) result);
			DetailFragmentBuilder.buildListAndAttach(context, 
					(ViewGroup) view.findViewById(R.id.projectCommentContent), adapter);
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

	/* (non-Javadoc)
	 * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
	 */
	@Override
	public void onTabChanged(String tabId) {
		
		if(project==null) return;
		
		if(tabId.equals(DetailFragmentBuilder.TAB_DETAILS)){
			DetailFragmentBuilder.buildDetails(getView(), imageView, screenWidth, project);
		}else if(tabId.equals(DetailFragmentBuilder.TAB_TIERS)){
			new TierDataLoader(context, client, this, project.getRef()).execute();
		}else if(tabId.equals(DetailFragmentBuilder.TAB_UPDATES)){
			new UpdateDataLoader(context, client, this, project.getRef()).execute();
		}else if(tabId.equals(DetailFragmentBuilder.TAB_COMMENTS)){
			new CommentDataLoader(context, client, this, project.getRef()).execute();
		}
		
	}

}
