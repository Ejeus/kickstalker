/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.adapter;

import java.util.List;

import org.hummer.kickstalker.data.Reference;
import org.hummer.kickstalker.view.ProjectCardView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * An adapter to display project info cards. Use with a 
 * GridView to get good visual results.
 * 
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class ProjectCardAdapter extends BaseAdapter {

	private Context context;
	private List<Reference> projects;
	
	/**
	 * @param context, Context. The current context.
	 * @param projects, List. The list of project <code>Reference</code>s to show.
	 */
	public void setData(Context context, List<Reference> projects){
		this.context = context;
		this.projects = projects;
		notifyDataSetChanged();
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return projects.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return projects.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Reference ref = projects.get(position);
		if(ProjectCardView.class.isInstance(convertView)){
			((ProjectCardView)convertView).setProjectReference(ref);
			return convertView;
		} else {
			return new ProjectCardView(context, ref);
		}
	}

}
