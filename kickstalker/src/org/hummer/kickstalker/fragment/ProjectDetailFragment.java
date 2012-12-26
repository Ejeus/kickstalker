/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.fragment;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.hummer.kickstalker.R;
import org.hummer.kickstalker.adapter.CommentAdapter;
import org.hummer.kickstalker.client.KickstarterClient;
import org.hummer.kickstalker.data.Comment;
import org.hummer.kickstalker.data.Project;
import org.hummer.kickstalker.util.TimeUtil;

import android.app.Fragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class ProjectDetailFragment extends Fragment implements OnTabChangeListener {

	public static final String KEY_PROJECT = "KEY_PROJECT";
	public static final String TAG = "PRJDTLFR";
	
	static final String TAB_DETAILS = "Details";
	static final String TAB_UPDATES = "Updates";
	static final String TAB_COMMENTS = "Comments";
	
	private KickstarterClient client;
	private Project project;
	private CommentAdapter comments;
	private NumberFormat nF;
	private NumberFormat cF;
	private NumberFormat pF;
	boolean updatesLoaded = false;
	boolean commentsLoaded = false;

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
		
		TabSpec details = tabHost.newTabSpec(TAB_DETAILS);
		details.setIndicator("Details");
		details.setContent(R.id.projectDetailContent);
		tabHost.addTab(details);
		
		TabSpec updates = tabHost.newTabSpec(TAB_UPDATES);
		updates.setIndicator("Updates");
		updates.setContent(R.id.projectUpdateContent);
		tabHost.addTab(updates);
		
		TabSpec comments = tabHost.newTabSpec(TAB_COMMENTS);
		comments.setIndicator("Comments");
		comments.setContent(R.id.projectCommentContent);
		tabHost.addTab(comments);
		
		tabHost.setCurrentTab(0);
		tabHost.setOnTabChangedListener(this);
		return view;
	}

	
	private void loadValues(View parent){
		
		int pldg = project.getPledged();
		int gl = project.getGoal();
		int prg = pldg > gl ? gl : pldg;
		
		TextView title = (TextView) parent.findViewById(R.id.fieldTitle);
		title.setText(project.getTitle());
		
		ProgressBar progress = (ProgressBar) 
				parent.findViewById(R.id.progressFunding);
		
		progress.setMax(gl);
		progress.setProgress(prg);
		
		ImageView img = (ImageView) parent.findViewById(R.id.projectImage);
		img.setImageBitmap(scaleImage());
		
		TextView shortDescription = (TextView) parent.findViewById(
				R.id.fieldShortDescription);
		shortDescription.setText(project.getShortDescription());
		
		TextView backers = (TextView) parent.findViewById(R.id.fieldBackers);
		backers.setText(nF.format(project.getBackers()));
		
		TextView pledged = (TextView) parent.findViewById(R.id.fieldPledged);
		pledged.setText(cF.format(pldg));
		
		TextView percent = (TextView) parent.findViewById(R.id.fieldPercent);
		percent.setText(" (" + pF.format(project.getPercent()) + ")");
		
		TextView goal = (TextView) parent.findViewById(R.id.fieldGoal);
		goal.setText(cF.format(gl));
		
		TextView timeLeft = (TextView) parent.findViewById(R.id.fieldTimeLeft);
		timeLeft.setText(TimeUtil.hoursToReadable(project.getTimeLeft()));
		
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
		
		int tw = size.x;
		int th = 400;
		float factor = ((float)bm.getWidth() / tw);
		
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
		Bitmap scaled = Bitmap.createBitmap(tw, th, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(scaled);
		Rect source = new Rect(0, 0, bm.getWidth(), 
				Float.valueOf(th * factor).intValue());
		Rect dest = new Rect(0, 0, tw, th);
		
		c.drawBitmap(bm, source, dest, p);
		
		return scaled;
		
	}

	public void refreshComments(List<Comment> comments){
		
		Context context = getActivity();
		this.comments = new CommentAdapter();
		this.comments.setData(context, comments);
		
		ListView list = new ListView(context);
		list.setAdapter(this.comments);
		
		LinearLayout parent = (LinearLayout) 
				getView().findViewById(R.id.projectCommentContent);
		
		parent.removeAllViews();
		parent.addView(list);
		
		
	}

	/* (non-Javadoc)
	 * @see android.widget.TabHost.OnTabChangeListener#onTabChanged(java.lang.String)
	 */
	@Override
	public void onTabChanged(String tabId) {
		
		if(tabId.equals(TAB_COMMENTS) && !commentsLoaded){
			new CommentsDataLoader().execute();
		}
		
	}
	
	private class CommentsDataLoader extends AsyncTask<Void, Void, List<Comment>>{

		/* (non-Javadoc)
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		@Override
		protected List<Comment> doInBackground(Void... params) {
			try {
				return client.getCommentsFor(getActivity(), project.getRef());
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return new ArrayList<Comment>();
		}

		@Override
		protected void onPostExecute(List<Comment> result) {
			refreshComments(result);
		}
		
		
		
	}
}
