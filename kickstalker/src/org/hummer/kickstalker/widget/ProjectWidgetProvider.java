package org.hummer.kickstalker.widget;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.text.NumberFormat;

import org.hummer.kickstalker.R;
import org.hummer.kickstalker.activity.ProjectDetailActivity;
import org.hummer.kickstalker.client.KickstarterClient;
import org.hummer.kickstalker.data.Project;
import org.hummer.kickstalker.data.Reference;
import org.hummer.kickstalker.factory.WidgetDataFactory;
import org.hummer.kickstalker.util.MediaUtil;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

public class ProjectWidgetProvider extends AppWidgetProvider /*implements TaskCallbackI*/ {

	public static final String TAG = "PRJWDGTPRV";
	private static final int WIDGET_SIZE = 110;
	private Context context;
	private AppWidgetManager appWidgetManager;
	static NumberFormat nF = NumberFormat.getInstance();
	
	
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		int N = appWidgetIds.length;
		WidgetDataMap data=null;
		
		try {
			data = WidgetDataFactory.load(context);
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		for(int i=0;i<N;i++)
			if(data!=null) data.remove(appWidgetIds[i]);
			
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		
		this.context = context;
		this.appWidgetManager = appWidgetManager;
		final int N = appWidgetIds.length;
		nF  = NumberFormat.getInstance();
		
		try {
			WidgetDataMap data = WidgetDataFactory.load(context);
			KickstarterClient client = new KickstarterClient(context);

			for(int i=0;i<N; i++){

				int widgetId = appWidgetIds[i];
				Reference ref = data.get(widgetId);
				if(ref==null) return;
				Project prj = client.getProjectFromRef(context, ref);
				update(prj, widgetId);

			}

		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}


	}
	
	public void update(Project project, int widgetId){
		RemoteViews views = buildRemoteViews(context, project, widgetId);
		appWidgetManager.updateAppWidget(widgetId, views);
	}
	
	public static RemoteViews buildRemoteViews(Context context, Project project,
			int widgetId){
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.widget_project);
		
		Bitmap bm = MediaUtil.createBitmap(project.getImageData(), 
				WIDGET_SIZE, WIDGET_SIZE);
		views.setImageViewBitmap(R.id.projectImage, bm);
		views.setProgressBar(R.id.progressFunding, project.getGoal(), 
				project.getPledged(), false);
		views.setTextViewText(R.id.fieldWidgetText, textWidget(project));
		
		Intent i = new Intent(context, ProjectDetailActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra(ProjectDetailActivity.KEY_PRJREF, project.asReference());
		PendingIntent pi = PendingIntent.getActivity(context, widgetId, i, 
				PendingIntent.FLAG_UPDATE_CURRENT);
		
		views.setOnClickPendingIntent(R.id.projectImage, pi);
		
		return views;
	}
	
	public static String textWidget(Project project){
		return project.getTitle();
	}

}
