/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.activity;

import java.io.IOException;
import java.io.StreamCorruptedException;

import org.hummer.kickstalker.AppController;
import org.hummer.kickstalker.R;
import org.hummer.kickstalker.activity.BaseActivity.Phase;
import org.hummer.kickstalker.client.KickstarterClient;
import org.hummer.kickstalker.data.BookmarkBundle;
import org.hummer.kickstalker.data.BookmarkBundle.BookmarkType;
import org.hummer.kickstalker.data.Project;
import org.hummer.kickstalker.data.Reference;
import org.hummer.kickstalker.factory.WidgetDataFactory;
import org.hummer.kickstalker.task.AbstractTask;
import org.hummer.kickstalker.task.DetailDataLoader;
import org.hummer.kickstalker.task.i.TaskCallbackI;
import org.hummer.kickstalker.widget.ProjectWidgetProvider;
import org.hummer.kickstalker.widget.WidgetDataMap;

import android.app.ListActivity;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RemoteViews;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class WidgetProjectConfigurationActivity extends ListActivity implements TaskCallbackI {

	public static final String TAG = "WPCA";
	
	private KickstarterClient client;
	private Intent origin;
	private Bundle extras;
	private int widgetId;
	private BookmarkBundle projectBookmarks;
	private WidgetDataMap wdm;
	private Phase phase;
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.widget_project_config);
		client = new KickstarterClient(this);
		
		((Button)findViewById(R.id.buttonCancel)).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				onCancel();
			}
			
		});
		
		origin = getIntent();
		extras = origin.getExtras();
		widgetId = extras.getInt(
				AppWidgetManager.EXTRA_APPWIDGET_ID,
				AppWidgetManager.INVALID_APPWIDGET_ID);
		
		projectBookmarks = AppController.getInstance().
				getBookmarks(this, BookmarkType.PROJECT);
		setListAdapter(new ArrayAdapter<Reference>(
				this, R.layout.list_detail_reference, projectBookmarks));
		
		try {
			wdm = WidgetDataFactory.load(this);
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		if(wdm==null) wdm = new WidgetDataMap();
		
		phase = Phase.IDLE;
	}
	
	
	
	/* (non-Javadoc)
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		if(phase!=Phase.IDLE) return;
		Reference ref = projectBookmarks.get(position);
		wdm.put(widgetId, ref);
		
		new DetailDataLoader(this, client, this).execute(ref);
	}
	
	/**
	 * If user cancelled adding of a widget by not selecting a list value.
	 */
	protected void onCancel(){
		Intent result = new Intent();
		result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
		setResult(RESULT_CANCELED, result);
		finish();
	}



	/**
	 * @param project, Project. The project to create a widget from.
	 */
	protected void updateWidget(Project project){
		
		try {
			WidgetDataFactory.store(this, wdm);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		AppWidgetManager awM = AppWidgetManager.getInstance(this);
		
		RemoteViews views = new ProjectWidgetProvider().buildRemoteViews(this, 
				getPackageName(), project, widgetId);
		awM.updateAppWidget(widgetId, views);
		
		Intent result = new Intent();
		result.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
		setResult(RESULT_OK, result);
		finish();
	}



	/* (non-Javadoc)
	 * @see org.hummer.kickstalker.task.i.TaskCallbackI#onTaskStarted(org.hummer.kickstalker.task.AbstractTask)
	 */
	@Override
	public void onTaskStarted(AbstractTask<?, ?, ?> task) {
		phase = Phase.BUSY;
	}



	/* (non-Javadoc)
	 * @see org.hummer.kickstalker.task.i.TaskCallbackI#onTaskFinished(org.hummer.kickstalker.task.AbstractTask, java.lang.Object)
	 */
	@Override
	public void onTaskFinished(AbstractTask<?, ?, ?> task, Object result) {
		
		updateWidget((Project)result);
		phase = Phase.IDLE;
	}



	/* (non-Javadoc)
	 * @see org.hummer.kickstalker.task.i.TaskCallbackI#onTaskCancelled(org.hummer.kickstalker.task.AbstractTask)
	 */
	@Override
	public void onTaskCancelled(AbstractTask<?, ?, ?> task) {
		phase = Phase.IDLE;
	}
	
}
