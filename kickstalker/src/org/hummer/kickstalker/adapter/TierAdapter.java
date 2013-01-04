/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.adapter;

import java.util.List;

import org.hummer.kickstalker.R;
import org.hummer.kickstalker.data.Tier;

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
 * Shows tiers for a project.
 * 
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class TierAdapter extends BaseAdapter {

	Context context;
	List<Tier> tiers;
	
	/**
	 * @param context, Context. The current context.
	 * @param tiers, List. The list of tiers to show.
	 */
	public void setData(Context context, List<Tier> tiers){
		this.context = context;
		this.tiers = tiers;
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return tiers.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return tiers.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public boolean isEnabled(int position){
		return false;
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Tier c = tiers.get(position);
		TierViewHolder viewHolder;
		
		if(convertView==null){
			LayoutInflater inflater = (LayoutInflater) 
					context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			viewHolder = new TierViewHolder();
			convertView = inflater.inflate(R.layout.list_detail_tier, parent, false);
			viewHolder.title = (TextView) convertView.findViewById(R.id.tierTitle);
			viewHolder.backers = (TextView) convertView.findViewById(R.id.tierBackers);
			viewHolder.body = (TextView) convertView.findViewById(R.id.tierBody);
			viewHolder.text = Html.fromHtml(c.getBody());
			viewHolder.body.setAutoLinkMask(Linkify.WEB_URLS);
			viewHolder.body.setLinksClickable(true);
			convertView.setTag(viewHolder);
		} else viewHolder = (TierViewHolder) convertView.getTag();
		
		viewHolder.title.setText(c.getTitle());
		viewHolder.backers.setText(c.getBackers());
		viewHolder.body.setText(Html.fromHtml(c.getBody()));
		viewHolder.body.setText(viewHolder.text);

		
		return convertView;
		
	}
	
	static class TierViewHolder{
		TextView title;
		TextView backers;
		TextView body;
		Spanned text;
	}

}
