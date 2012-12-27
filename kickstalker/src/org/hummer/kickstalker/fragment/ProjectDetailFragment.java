/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.fragment;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.hummer.kickstalker.R;
import org.hummer.kickstalker.action.OpenWebsiteAction;
import org.hummer.kickstalker.adapter.CommentAdapter;
import org.hummer.kickstalker.adapter.TierAdapter;
import org.hummer.kickstalker.adapter.UpdateAdapter;
import org.hummer.kickstalker.client.KickstarterClient;
import org.hummer.kickstalker.data.Comment;
import org.hummer.kickstalker.data.Project;
import org.hummer.kickstalker.data.Tier;
import org.hummer.kickstalker.data.Update;
import org.hummer.kickstalker.task.AbstractTask;
import org.hummer.kickstalker.task.CommentDataLoader;
import org.hummer.kickstalker.task.TierDataLoader;
import org.hummer.kickstalker.task.UpdateDataLoader;
import org.hummer.kickstalker.task.i.TaskCallbackI;
import org.hummer.kickstalker.util.ImageUtil;
import org.hummer.kickstalker.util.TimeUtil;
import org.hummer.kickstalker.util.ViewUtil;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TextView;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class ProjectDetailFragment extends Fragment implements 
	OnTabChangeListener, TaskCallbackI {

	public static final String KEY_PROJECT = "KEY_PROJECT";
	public static final String TAG = "PRJDTLFR";
	
	static final String TAB_DETAILS = "Details";
	static final String TAB_TIERS = "Tiers";
	static final String TAB_UPDATES = "Updates";
	static final String TAB_COMMENTS = "Comments";
	
	private KickstarterClient client;
	private Project project;
	private CommentAdapter comments;
	private TierAdapter tiers;
	private UpdateAdapter updates;
	private NumberFormat nF;
	private NumberFormat cF;
	private NumberFormat pF;

	public ProjectDetailFragment(){
		project = null;
		nF = NumberFormat.getInstance();
		cF = NumberFormat.getCurrencyInstance(Locale.US);
		pF = NumberFormat.getPercentInstance();
	}
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		Bundle args = getArguments();
		if(args!=null){
			if(args.containsKey(KEY_PROJECT)){
				project = (Project) args.getSerializable(KEY_PROJECT);
			}
		}
		
		super.onCreate(savedInstanceState);
		
		client = new KickstarterClient(getActivity());
	}



	@Override
	public void onResume() {

		super.onResume();
		loadValues(getView());
		
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(
				R.layout.fragment_detail_project_tabbed, container, false);
		TabHost tabHost = (TabHost) view.findViewById(android.R.id.tabhost);
		tabHost.setup();
		
		ViewUtil.addTabToTabHost(tabHost, TAB_DETAILS, R.id.projectDetailContent);
		ViewUtil.addTabToTabHost(tabHost, TAB_TIERS, R.id.projectTierContent);
		ViewUtil.addTabToTabHost(tabHost, TAB_UPDATES, R.id.projectUpdateContent);
		ViewUtil.addTabToTabHost(tabHost, TAB_COMMENTS, R.id.projectCommentContent);
		
		tabHost.setCurrentTab(0);
		tabHost.setOnTabChangedListener(this);
		return view;
	}
	
	private void loadValues(View parent){
		
		Activity activity = getActivity();
		int pldg = project.getPledged();
		int gl = project.getGoal();
		int prg = pldg > gl ? gl : pldg;
		
		TextView title = ViewUtil.findAndSetText(activity, R.id.fieldTitle, 
				project.getTitle());
		title.setOnClickListener(new OpenWebsiteAction(activity, 
				project.getRef()));
		
		ProgressBar progress = (ProgressBar) 
				parent.findViewById(R.id.progressFunding);
		
		progress.setMax(gl);
		progress.setProgress(prg);
		
		ImageView img = (ImageView) parent.findViewById(R.id.projectImage);
		img.setImageBitmap(scaleImage());
		
		ViewUtil.findAndSetText(activity, R.id.fieldShortDescription, 
				project.getShortDescription());
		ViewUtil.findAndSetText(activity, R.id.fieldBackers, 
				nF.format(project.getBackers()));
		ViewUtil.findAndSetText(activity, R.id.fieldPledged, 
				cF.format(pldg));
		ViewUtil.findAndSetText(activity, R.id.fieldPercent,
				" (" + pF.format(project.getPercent()) + ")");
		ViewUtil.findAndSetText(activity, R.id.fieldGoal, cF.format(gl));
		ViewUtil.findAndSetText(activity, R.id.fieldTimeLeft,
				TimeUtil.hoursToReadable(project.getTimeLeft()));
		
		TextView description = (TextView) parent.findViewById(R.id.fieldDescription);
		Spanned spanned = Html.fromHtml(project.getDescription());
		description.setText(spanned);
		
	}



	/**
	 * @return Bitmap - A scaled image fitting the details image panel.
	 */
	private Bitmap scaleImage() {
		
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		byte[] imgdata = project.getImageData();
		Bitmap bm = BitmapFactory.decodeByteArray(imgdata, 0, imgdata.length);
		
		return ImageUtil.scaleImage(bm, size.x, 400);
		
	}

	public void refreshComments(List<Comment> comments){
		
		Context context = getActivity();
		this.comments = new CommentAdapter();
		this.comments.setData(context, comments);
		refresh(context, this.comments, R.id.projectCommentContent);
		
		
	}
	
	public void refreshTiers(List<Tier> tiers){
		
		Context context = getActivity();
		this.tiers = new TierAdapter();
		this.tiers.setData(context, tiers);
		refresh(context, this.tiers, R.id.projectTierContent);
		
	}
	
	public void refreshUpdates(List<Update> updates){
		Context context = getActivity();
		this.updates = new UpdateAdapter();
		this.updates.setData(context, updates);
		refresh(context, this.updates, R.id.projectUpdateContent);
		
	}
	
	public void refresh(Context context, BaseAdapter adapter, int parentId){
		
		ListView list = new ListView(context);
		list.setAdapter(adapter);
		
		LinearLayout parent = (LinearLayout)
				getView().findViewById(parentId);
		
		parent.removeAllViews();
		parent.addView(list);
		
	}

	/* (non-Javadoc)
	 * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
	 */
	@Override
	public void onTabChanged(String tabId) {
		
		if(tabId.equals(TAB_COMMENTS)){
			new CommentDataLoader(getActivity(), client, this, project.getRef()).execute();
		}else if(tabId.equals(TAB_TIERS)){
			new TierDataLoader(getActivity(), client, this, project.getRef()).execute();
		}else if(tabId.equals(TAB_UPDATES)){
			new UpdateDataLoader(getActivity(), client, this, project.getRef()).execute();
		}
		
	}



	/* (non-Javadoc)
	 * @see org.hummer.kickstalker.task.i.TaskCallbackI#onTaskFinished(android.os.AsyncTask, java.lang.Object)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void onTaskFinished(AbstractTask<?, ?, ?> task, Object result) {
		
		if(task.getName().equals(CommentDataLoader.TASKNAME)){
			refreshComments((List<Comment>) result);
		}else if(task.getName().equals(TierDataLoader.TASKNAME)){
			refreshTiers((List<Tier>) result);
		}else if(task.getName().equals(UpdateDataLoader.TASKNAME)){
			refreshUpdates((List<Update>) result);
		}
		
	}
	
}
