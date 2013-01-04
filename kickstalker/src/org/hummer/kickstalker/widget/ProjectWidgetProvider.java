package org.hummer.kickstalker.widget;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import org.hummer.kickstalker.R;
import org.hummer.kickstalker.activity.ProjectDetailActivity;
import org.hummer.kickstalker.client.KickstarterClient;
import org.hummer.kickstalker.data.Project;
import org.hummer.kickstalker.data.Reference;
import org.hummer.kickstalker.factory.WidgetDataFactory;
import org.hummer.kickstalker.task.AbstractTask;
import org.hummer.kickstalker.task.DetailDataLoader;
import org.hummer.kickstalker.task.i.TaskCallbackI;
import org.hummer.kickstalker.util.MediaUtil;
import org.hummer.kickstalker.util.StringUtil;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.RemoteViews;

public class ProjectWidgetProvider extends AppWidgetProvider implements TaskCallbackI {

	public static final String TAG = "PRJWDGTPRV";
	private static final int WIDGET_SIZE = 108;
	private Context context;
	private AppWidgetManager appWidgetManager;
	private WidgetDataMap data;
	private KickstarterClient client;
	private Map<String, Integer> widgetIds;
	private String pkgName;
	static NumberFormat nF = NumberFormat.getInstance();

	public ProjectWidgetProvider(){
		widgetIds = new HashMap<String, Integer>();
	}

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

	public void init(Context context) throws 
	StreamCorruptedException, IOException, ClassNotFoundException{
		data = WidgetDataFactory.load(context);
		client = new KickstarterClient(context);
	}

	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		this.context = context;
		pkgName = context.getPackageName();
		this.appWidgetManager = appWidgetManager;
		final int N = appWidgetIds.length;
		nF  = NumberFormat.getInstance();

		try {

			init(context);
			for(int i=0;i<N; i++)
				prepareUpdate(appWidgetIds[i]);

		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}


	}

	public void prepareUpdate(int widgetId) throws IOException{
		Reference ref = data.get(widgetId);
		if(ref==null) return;
		widgetIds.put(ref.getRef(), widgetId);
		new DetailDataLoader(context, client, this).execute(ref);

	}
	
	public void update(Project project){
		int widgetId = widgetIds.get(project.getRef());
		RemoteViews views = buildRemoteViews(context, pkgName, 
				project, widgetId);
		appWidgetManager.updateAppWidget(widgetId, views);
	}

	public RemoteViews buildRemoteViews(Context context, String pkgName, Project project,
			int widgetId){
		
		RemoteViews views = new RemoteViews(pkgName,
				R.layout.widget_project);

		Bitmap bm = MediaUtil.createBitmap(project.getImageData(), 
				WIDGET_SIZE, WIDGET_SIZE);
		views.setImageViewBitmap(R.id.projectImage, bm);
		setupProgressBar(views, project);
		views.setTextViewText(R.id.fieldWidgetText, textWidget(project));

		Intent i = new Intent(context, ProjectDetailActivity.class);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.putExtra(ProjectDetailActivity.KEY_PRJREF, project.asReference());
		PendingIntent pi = PendingIntent.getActivity(context, widgetId, i, 
				PendingIntent.FLAG_UPDATE_CURRENT);

		views.setOnClickPendingIntent(R.id.projectImage, pi);

		return views;
	}

	private void setupProgressBar(RemoteViews views, Project project){
		
		int[] progressIds = new int[]{
				R.id.progressFundingRunning,
				R.id.progressFundingCompleted,
				R.id.progressFundingFailed
		};
		
		int relevantId;
		switch(project.getStatus()){
		case RUNNING:
			relevantId = R.id.progressFundingRunning;
			break;
		case COMPLETED:
			relevantId = R.id.progressFundingCompleted;
			break;
		default:
			relevantId = R.id.progressFundingFailed;
		}
		
		for(int i=0;i<progressIds.length;i++){
			int id = progressIds[i];
			if(id!=relevantId) views.setViewVisibility(id, View.GONE);
		}
		
		views.setProgressBar(relevantId, project.getGoal(), 
				project.getPledged(), false);
		
	}
	
	public static String textWidget(Project project){
		return StringUtil.shorten(project.getTitle(), 25);
	}

	/* (non-Javadoc)
	 * @see org.hummer.kickstalker.task.i.TaskCallbackI#onTaskStarted(org.hummer.kickstalker.task.AbstractTask)
	 */
	@Override
	public void onTaskStarted(AbstractTask<?, ?, ?> task) {}

	/* (non-Javadoc)
	 * @see org.hummer.kickstalker.task.i.TaskCallbackI#onTaskFinished(org.hummer.kickstalker.task.AbstractTask, java.lang.Object)
	 */
	@Override
	public void onTaskFinished(AbstractTask<?, ?, ?> task, Object result) {
		update((Project) result);
	}

	/* (non-Javadoc)
	 * @see org.hummer.kickstalker.task.i.TaskCallbackI#onTaskCancelled(org.hummer.kickstalker.task.AbstractTask)
	 */
	@Override
	public void onTaskCancelled(AbstractTask<?, ?, ?> task) {}
	
	

}
