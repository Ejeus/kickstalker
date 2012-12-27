/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.util;

import android.app.Activity;
import android.view.View;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class ViewUtil {

	public static void addTabToTabHost(TabHost host, String label, int content){
		
		TabSpec spec = host.newTabSpec(label);
		spec.setIndicator(label);
		spec.setContent(content);
		host.addTab(spec);
		
	}
	
	public static TextView findAndSetText(Activity activity, int id, String value){
		
		TextView view = (TextView) activity.findViewById(id);
		view.setText(value);
		return view;
		
	}

	/**
	 * @param convertView
	 * @param id
	 * @param value
	 */
	public static TextView findAndSetText(View parent, int id,
			String value) {
		
		TextView view = (TextView) parent.findViewById(id);
		view.setText(value);
		return view;
		
	}
}
