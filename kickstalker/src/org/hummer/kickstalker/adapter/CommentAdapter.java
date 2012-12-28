/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.adapter;

import java.util.List;

import org.hummer.kickstalker.R;
import org.hummer.kickstalker.activity.ProjectListActivity;
import org.hummer.kickstalker.data.Comment;
import org.hummer.kickstalker.fragment.KickstarterListFragment;
import org.hummer.kickstalker.util.StringUtil;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.text.Spanned;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class CommentAdapter extends BaseAdapter {

	Context context;
	List<Comment> comments;
	
	public void setData(Context context, List<Comment> comments){
		this.context = context;
		this.comments = comments;
	}
	
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return comments.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return comments.get(position);
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
		Comment c = comments.get(position);
		final String displayname = c.getAuthor().getLabel();
		final String username = c.getAuthor().getRef();
		
		if(convertView==null){
			LayoutInflater inflater = (LayoutInflater) 
					context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			convertView = inflater.inflate(R.layout.list_detail_comment, parent, false);
		}
		
		TextView author = (TextView) convertView.findViewById(R.id.commentAuthor);
		author.setText(displayname);
		author.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent i = new Intent(context, ProjectListActivity.class);
				i.putExtra(KickstarterListFragment.KEY_TYPE, 
						KickstarterListFragment.TYPE_BACKED);
				i.putExtra(KickstarterListFragment.KEY_USERNAME, username);
				i.putExtra(KickstarterListFragment.KEY_TITLE, 
						StringUtil.getOwnership(displayname) + " " +
								context.getResources().getString(
										R.string.ac_backed_projects));
				context.startActivity(i);
			}
			
		});
		
		TextView date = (TextView) convertView.findViewById(R.id.commentDate);
		date.setText(c.getDate());
		
		TextView comment = (TextView) convertView.findViewById(R.id.commentContent);
		Spanned spanned = Html.fromHtml(c.getContent());
		comment.setText(spanned);
		comment.setAutoLinkMask(Linkify.WEB_URLS);
		comment.setLinksClickable(true);
		
		return convertView;
		
	}

}
