/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.adapter;

import java.util.List;

import org.hummer.kickstalker.R;
import org.hummer.kickstalker.client.KickstarterClient;
import org.hummer.kickstalker.data.Update;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
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
		UpdateViewHolder viewHolder;
		
		if(convertView==null){
			LayoutInflater inflater = (LayoutInflater) 
					context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			viewHolder = new UpdateViewHolder();
			convertView = inflater.inflate(R.layout.list_detail_update, parent, false);
			viewHolder.title = (TextView) convertView.findViewById(R.id.updateTitle);
			viewHolder.id = (TextView) convertView.findViewById(R.id.updateId);
			viewHolder.body = (WebView) convertView.findViewById(R.id.updateBody);
			convertView.setTag(viewHolder);
		} else viewHolder = (UpdateViewHolder) convertView.getTag();
		
		viewHolder.title.setText(u.getTitle());
		viewHolder.id.setText(u.getUpdateId());
		viewHolder.body.loadDataWithBaseURL(
				KickstarterClient.BASE_URL, u.getBody(), 
				"text/html", "UTF-8", "");
		
		return convertView;
		
	}
	
	static class UpdateViewHolder{
		TextView title;
		TextView id;
		WebView body;
	}

}
