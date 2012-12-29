/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.activity;

import java.io.IOException;
import java.util.Collections;

import org.hummer.kickstalker.AppController;
import org.hummer.kickstalker.R;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class BaseActivity extends Activity {

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
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		appC = AppController.getInstance();
		setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		phase = Phase.IDLE;
		
	}
	
	public AppController getAppController(){
		return appC;
	}
	
	public BookmarkBundle getProjectBookmarks(){
		return projectBookmarks;
	}
	
	public ArrayAdapter<Reference> getStarredProjects(){
		return starredProjects;
	}
	
	public BookmarkBundle getPeopleBookmarks(){
		return peopleBookmarks;
	}
	
	public ArrayAdapter<Reference> getStarredPeople(){
		return starredPeople;
	}
	
	
	
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		boolean returnVal = super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.options_app, menu);
		return returnVal;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		boolean returnVal = super.onOptionsItemSelected(item);
		Intent i;
		
		if(!returnVal){
			switch(item.getItemId()){
			case R.id.ac_config:
				i = new Intent(this, ConfigurationActivity.class);
				startActivity(i);
				return true;
			case R.id.ac_favorite_projects:
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle(R.string.starred_projects)
				.setAdapter(starredProjects, new OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
								startActivityForProjectRef(
										projectBookmarks.get(which));			
					}
					
				}).create().show();
			}
		}
		
		return returnVal;
	}
	
	
	
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

	protected void startActivityForProjectRef(Reference ref){
		Intent i = new Intent(this, ProjectDetailActivity.class);
		i.putExtra(ProjectDetailActivity.KEY_PRJREF, ref);
		startActivity(i);
	}
	
}
