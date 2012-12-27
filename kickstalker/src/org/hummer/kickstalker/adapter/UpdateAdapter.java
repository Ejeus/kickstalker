/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.adapter;

import java.util.List;

import org.hummer.kickstalker.R;
import org.hummer.kickstalker.data.Update;
import org.hummer.kickstalker.util.ViewUtil;

import android.content.Context;
import android.text.Html;
import android.text.Spanned;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class UpdateAdapter extends BaseAdapter {

	Context context;
	List<Update> updates;
	
	public void setData(Context context, List<Update> comments){
		this.context = context;
		this.updates = comments;
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return updates.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return updates.get(position);
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
		Update u = updates.get(position);
		
		if(convertView==null){
			LayoutInflater inflater = (LayoutInflater) 
					context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			convertView = inflater.inflate(R.layout.list_detail_update, parent, false);
		}
		
		ViewUtil.findAndSetText(convertView, R.id.updateTitle, u.getTitle());
		ViewUtil.findAndSetText(convertView, R.id.updateId, u.getUpdateId());
		
		TextView body = (TextView) convertView.findViewById(R.id.updateBody);
		Spanned spanned = Html.fromHtml(u.getBody());
		body.setText(spanned);
		body.setAutoLinkMask(Linkify.WEB_URLS);
		body.setLinksClickable(true);
		
		return convertView;
		
	}

}
