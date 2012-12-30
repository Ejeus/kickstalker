package org.hummer.kickstalker.widget;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import org.hummer.kickstalker.R;
import org.hummer.kickstalker.client.KickstarterClient;
import org.hummer.kickstalker.data.Project;
import org.hummer.kickstalker.data.Reference;
import org.hummer.kickstalker.factory.WidgetDataFactory;
import org.hummer.kickstalker.util.MediaUtil;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.RemoteViews;

public class ProjectWidgetProvider extends AppWidgetProvider /*implements TaskCallbackI*/ {

	private static final String TAG = "PRJWDGTPRV";
	private Map<Reference, Integer> widgets;
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
		widgets = new HashMap<Reference, Integer>();
		nF  = NumberFormat.getInstance();
		nF.setGroupingUsed(true);
		nF.setMaximumFractionDigits(0);
		
		try {
			WidgetDataMap data = WidgetDataFactory.load(context);
			KickstarterClient client = new KickstarterClient(context);

			for(int i=0;i<N; i++){

				int widgetId = appWidgetIds[i];
				Reference ref = data.get(widgetId);
				Log.i(TAG, "Working on widget id " + widgetId + ": " + 
						Boolean.valueOf(ref==null).toString());
				if(ref==null) return;
				widgets.put(ref, widgetId);
				Project prj = client.getProjectFromRef(context, ref);
				update(prj);

			}

		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}


	}
	
	public void update(Project project){
		Log.i(TAG, "Performing actual widget view update.");

		RemoteViews views = buildRemoteViews(context, project);
		appWidgetManager.updateAppWidget(widgets.get(project.asReference()), views);
	}
	
	public static RemoteViews buildRemoteViews(Context context, Project project){
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.widget_project);
		
		Bitmap bm = MediaUtil.createBitmap(project.getImageData(), 200, 200);
		views.setImageViewBitmap(R.id.projectImage, bm);
		views.setProgressBar(R.id.progressFunding, project.getGoal(), 
				project.getPledged(), false);
		views.setTextViewText(R.id.fieldTitle, project.getTitle());
		views.setTextViewText(R.id.fieldFunding, buildFundingText(project));
		return views;
	}
	
	private static String buildFundingText(Project project){
		return nF.format(project.getPledged()) + " / " + nF.format(project.getGoal());
	}

	/* (non-Javadoc)
	 * @see org.hummer.kickstalker.task.i.TaskCallbackI#onTaskStarted(org.hummer.kickstalker.task.AbstractTask)
	 *
	@Override
	public void onTaskStarted(AbstractTask<?, ?, ?> task) {}

	/* (non-Javadoc)
	 * @see org.hummer.kickstalker.task.i.TaskCallbackI#onTaskFinished(org.hummer.kickstalker.task.AbstractTask, java.lang.Object)
	 *
	@Override
	public void onTaskFinished(AbstractTask<?, ?, ?> task, Object result) {
		update((Project) result);
	}

	/* (non-Javadoc)
	 * @see org.hummer.kickstalker.task.i.TaskCallbackI#onTaskCancelled(org.hummer.kickstalker.task.AbstractTask)
	 *
	@Override
	public void onTaskCancelled(AbstractTask<?, ?, ?> task) {}
	*/



}
