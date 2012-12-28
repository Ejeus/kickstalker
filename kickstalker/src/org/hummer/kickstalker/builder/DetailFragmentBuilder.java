/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.builder;

import java.text.NumberFormat;
import java.util.Locale;

import org.hummer.kickstalker.R;
import org.hummer.kickstalker.data.Project;
import org.hummer.kickstalker.util.MediaUtil;
import org.hummer.kickstalker.util.TimeUtil;
import org.hummer.kickstalker.util.ViewUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class DetailFragmentBuilder {

	public static final String TAB_DETAILS = "Details";
	public static final String TAB_TIERS = "Tiers";
	public static final String TAB_UPDATES = "Updates";
	public static final String TAB_COMMENTS = "Comments";
	
	/**
	 * @param view
	 */
	public static void initialize(Context context, View view) {
		
		TabHost host = (TabHost) view.findViewById(android.R.id.tabhost);
		host.setup();
		
		ViewUtil.addTabToTabHost(host, TAB_DETAILS, R.id.projectDetailContent);
		ViewUtil.addTabToTabHost(host, TAB_TIERS, R.id.projectTierContent);
		ViewUtil.addTabToTabHost(host, TAB_UPDATES, R.id.projectUpdateContent);
		ViewUtil.addTabToTabHost(host, TAB_COMMENTS, R.id.projectCommentContent);
		
		clear(context, (ViewGroup) 
				view.findViewById(R.id.projectTierContent));
		clear(context, (ViewGroup) 
				view.findViewById(R.id.projectUpdateContent));
		clear(context, (ViewGroup) 
				view.findViewById(R.id.projectCommentContent));
		
	}
	
	public static void clearAll(Context context, View view, int[] ids){
		
		for(int i=0;i<ids.length;i++)
			clear(context, (ViewGroup) view.findViewById(ids[i]));
		
	}
	
	public static void clear(Context context, ViewGroup container){
		
		container.removeAllViews();
		ProgressBar prg = new ProgressBar(context);
		prg.setPadding(0, 50, 0, 0);
		container.addView(prg);
		
	}
	
	public static void buildDetails(View view, ImageView imageView, int tw, Project project){
		
		NumberFormat nF = NumberFormat.getInstance();
		NumberFormat cF = NumberFormat.getCurrencyInstance(Locale.US);
		NumberFormat pF = NumberFormat.getPercentInstance();
		
		byte[] imgdata = project.getImageData();
		Bitmap bm = BitmapFactory.decodeByteArray(imgdata, 0, imgdata.length);
		int th = bm.getHeight() * tw / bm.getWidth();
		imageView.setImageBitmap(
				MediaUtil.scaleImage(bm, tw, th));
		
		ViewUtil.findAndSetText(view, R.id.fieldTitle, project.getTitle());
		ViewUtil.findAndSetText(view, R.id.fieldCreator, project.getOwner().getLabel());
		ViewUtil.findAndSetText(view, R.id.fieldBackers, nF.format(project.getBackers()));
		ViewUtil.findAndSetText(view, R.id.fieldGoal, cF.format(project.getGoal()));
		ViewUtil.findAndSetText(view, R.id.fieldPercent, pF.format(project.getPercent()));
		ViewUtil.findAndSetText(view, R.id.fieldPledged, cF.format(project.getPledged()));
		ViewUtil.findAndSetText(view, R.id.fieldShortDescription, project.getShortDescription());
		ViewUtil.findAndSetText(view, R.id.fieldTimeLeft, TimeUtil.hoursToReadable(project.getTimeLeft()));
		ViewUtil.findAndSetText(view, R.id.fieldDescription, Html.fromHtml(project.getDescription()));
		
		view.invalidate();
		
	}
	
	public static void buildListAndAttach(Context context, ViewGroup parent, 
			BaseAdapter adapter){
		
		ListView view = new ListView(context);
		view.setAdapter(adapter);
		
		parent.removeAllViews();
		parent.addView(view);
		
	}

}
