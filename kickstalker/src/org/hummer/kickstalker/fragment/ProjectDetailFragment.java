/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.fragment;

import java.text.NumberFormat;

import org.hummer.kickstalker.R;
import org.hummer.kickstalker.data.Project;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class ProjectDetailFragment extends Fragment {

	public static final String KEY_PROJECT = "KEY_PROJECT";
	private Project project;
	private NumberFormat nF;
	private NumberFormat cF;

	public ProjectDetailFragment(){
		project = null;
		nF = NumberFormat.getInstance();
		cF = NumberFormat.getCurrencyInstance();
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
		
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_detail_project, 
				container, false);
		
		loadValues(view);
		return view;
	}

	
	private void loadValues(View parent){
		TextView title = (TextView) parent.findViewById(R.id.projectTitle);
		title.setText(project.getTitle());
		
		ImageView img = (ImageView) parent.findViewById(R.id.projectImage);
		img.setImageBitmap(scaleImage());
		
		TextView shortDescription = (TextView) parent.findViewById(
				R.id.fieldShortDescription);
		shortDescription.setText(project.getShortDescription());
		
		TextView backers = (TextView) parent.findViewById(R.id.fieldBackers);
		backers.setText(nF.format(project.getBackers()));
		
		TextView pledged = (TextView) parent.findViewById(R.id.fieldPledged);
		pledged.setText(cF.format(project.getPledged()));
		
		TextView timeLeft = (TextView) parent.findViewById(R.id.fieldTimeLeft);
		timeLeft.setText(nF.format(project.getTimeLeft()));
		
		TextView description = (TextView) parent.findViewById(R.id.fieldDescription);
		description.setText(project.getDescription());
		
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
}
