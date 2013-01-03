/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.activity;

import java.io.IOException;
import java.util.Collections;

import org.hummer.kickstalker.AppController;
import org.hummer.kickstalker.R;
import org.hummer.kickstalker.action.SearchAction;
import org.hummer.kickstalker.client.KickstarterClient;
import org.hummer.kickstalker.data.BookmarkBundle;
import org.hummer.kickstalker.data.BookmarkBundle.BookmarkType;
import org.hummer.kickstalker.data.Reference;
import org.hummer.kickstalker.factory.BookmarkFactory;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * Basic core Activity functionality for this app.
 * 
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public abstract class BaseActivity extends Activity {

	public static final String RETURN_TO = "NAV_RETURNTO";
	protected AppController appC;
	private BookmarkBundle projectBookmarks;
	private ArrayAdapter<Reference> starredProjects;
	private BookmarkBundle peopleBookmarks;
	private ArrayAdapter<Reference> starredPeople;
	protected Phase phase;

	public enum Phase{
		IDLE,
		BUSY,
		STOPPED
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		appC = AppController.getInstance();
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		phase = Phase.IDLE;

	}

	public abstract KickstarterClient getClient();

	/**
	 * @return AppController. Current instance of AppController.
	 */
	public AppController getAppController(){
		return appC;
	}

	/**
	 * @return BookmarkBundle. All bookmarked projects.
	 */
	public BookmarkBundle getProjectBookmarks(){
		return projectBookmarks;
	}

	/**
	 * @return ArrayAdapter. An adapter for visually presenting starred projects.
	 */
	public ArrayAdapter<Reference> getStarredProjects(){
		return starredProjects;
	}

	/**
	 * @return BookmarkBundle. All bookmarked people.
	 */
	public BookmarkBundle getPeopleBookmarks(){
		return peopleBookmarks;
	}

	/**
	 * @return ArrayAdapter. An adapter for visually presenting starred people.
	 */
	public ArrayAdapter<Reference> getStarredPeople(){
		return starredPeople;
	}



	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();

		projectBookmarks = appC.getBookmarks(this, BookmarkType.PROJECT);
		Collections.sort(projectBookmarks);
		starredProjects = new ArrayAdapter<Reference>(this, 
				R.layout.list_detail_reference, projectBookmarks);
		peopleBookmarks = appC.getBookmarks(this, BookmarkType.PERSON);
		Collections.sort(peopleBookmarks);
		starredPeople = new ArrayAdapter<Reference>(this,
				R.layout.list_detail_reference, peopleBookmarks);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean returnVal = super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_app, menu);
		return returnVal;
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean returnVal = super.onOptionsItemSelected(item);

		Intent i;
		AlertDialog.Builder builder;
		View view;

		if(!returnVal){
			switch(item.getItemId()){
			case R.id.ac_config:
				i = new Intent(this, ConfigurationActivity.class);
				startActivity(i);
				return true;
			case R.id.ac_favorite_projects:
				builder = new AlertDialog.Builder(this);
				builder.setTitle(R.string.starred_projects)
				.setAdapter(starredProjects, new OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						startActivityForProjectRef(
								projectBookmarks.get(which));			
					}

				}).create().show();
				return true;
			case R.id.ac_search:
				SearchAction searchAction = new SearchAction(this);
				view = LayoutInflater.from(this)
						.inflate(R.layout.dialog_search, null);
				((TextView)view.findViewById(R.id.fieldSearchTerm))
					.addTextChangedListener(searchAction);
				view.findViewById(R.id.buttonSearch)
					.setOnClickListener(searchAction);
				builder = new AlertDialog.Builder(this);
				AlertDialog dialog = builder.setTitle(R.string.title_search)
						.setView(view).create();
				
				searchAction.setDialog(dialog);
				dialog.show();
				return true;
			case R.id.ac_about:
				view = getWindow().getLayoutInflater()
				.inflate(R.layout.layout_license, null);
				builder = new AlertDialog.Builder(this);
				builder.setTitle(R.string.label_about)
				.setView(view)
				.create().show();
				return true;
			}
		}

		return returnVal;
	}



	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();

		try {
			BookmarkFactory.store(this, projectBookmarks);
			BookmarkFactory.store(this, peopleBookmarks);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param ref, Reference. The reference of a project to show details for.
	 */
	protected void startActivityForProjectRef(Reference ref){
		Intent i = new Intent(this, ProjectDetailActivity.class);
		i.putExtra(ProjectDetailActivity.KEY_PRJREF, ref);
		startActivity(i);
	}


	/**
	 * Call this if you want to return back to the main apps home page.
	 */
	public void home(){
		Intent i = new Intent(this, ProjectListActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
				Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(i);
		finish();
	}

}
