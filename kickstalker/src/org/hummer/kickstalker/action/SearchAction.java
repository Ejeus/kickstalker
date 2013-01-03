/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.action;

import org.hummer.kickstalker.R;
import org.hummer.kickstalker.activity.ProjectListActivity;
import org.hummer.kickstalker.fragment.KickstarterListFragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class SearchAction implements OnClickListener, TextWatcher {

	private Context context;
	private String filterValue;
	private AlertDialog dialog;

	public SearchAction(Context context){
		this.context = context;
	}
	
	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		
		if(dialog!=null && dialog.isShowing()) dialog.dismiss();
		if(filterValue==null || filterValue.equals("")) return;
		
		Intent i = new Intent(context, ProjectListActivity.class);
		i.putExtra(KickstarterListFragment.KEY_TITLE, 
				context.getResources().getString(R.string.label_search) + ": '" +
						filterValue + "'");
		i.putExtra(KickstarterListFragment.KEY_TYPE, 
				KickstarterListFragment.TYPE_SEARCH);
		i.putExtra(KickstarterListFragment.KEY_SEARCHTERM, filterValue);
		
		context.startActivity(i);

	}

	/* (non-Javadoc)
	 * @see android.text.TextWatcher#afterTextChanged(android.text.Editable)
	 */
	@Override
	public void afterTextChanged(Editable s) {
		filterValue = s.toString();
	}

	/* (non-Javadoc)
	 * @see android.text.TextWatcher#beforeTextChanged(java.lang.CharSequence, int, int, int)
	 */
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {}

	/* (non-Javadoc)
	 * @see android.text.TextWatcher#onTextChanged(java.lang.CharSequence, int, int, int)
	 */
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {}

	/**
	 * @param dialog
	 */
	public void setDialog(AlertDialog dialog) {
		this.dialog = dialog;
	}

}
