/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.adapter;

import java.util.List;

import org.hummer.kickstalker.R;
import org.hummer.kickstalker.data.Tier;
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
public class TierAdapter extends BaseAdapter {

	Context context;
	List<Tier> tiers;
	
	public void setData(Context context, List<Tier> comments){
		this.context = context;
		this.tiers = comments;
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

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Tier c = tiers.get(position);
		
		if(convertView==null){
			LayoutInflater inflater = (LayoutInflater) 
					context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			convertView = inflater.inflate(R.layout.list_detail_tier, parent, false);
		}
		
		ViewUtil.findAndSetText(convertView, R.id.tierTitle, c.getTitle());
		ViewUtil.findAndSetText(convertView, R.id.tierBackers, c.getBackers());
		
		TextView body = (TextView) convertView.findViewById(R.id.tierBody);
		Spanned spanned = Html.fromHtml(c.getBody());
		body.setText(spanned);
		body.setAutoLinkMask(Linkify.WEB_URLS);
		body.setLinksClickable(true);
		
		return convertView;
		
	}

}
