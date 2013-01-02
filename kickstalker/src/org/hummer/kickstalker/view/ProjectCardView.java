/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.view;

import org.hummer.kickstalker.R;
import org.hummer.kickstalker.data.Reference;
import org.hummer.kickstalker.task.AbstractTask;
import org.hummer.kickstalker.task.RemoteImageDataLoader;
import org.hummer.kickstalker.task.i.TaskCallbackI;
import org.hummer.kickstalker.util.MediaUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class ProjectCardView extends View implements TaskCallbackI {

	static final String TAG = "PRJCDVW";
	static final int PREFERRED_WIDTH = 355;
	static final int PREFERRED_HEIGHT = 305;
	private Context context;
	private Reference project;
	private static TextPaint paint;
	private static Paint titlePaint;
	private static Paint highlightPaint;
	private static Paint borderPaint;
	private static Paint shadowPaint;
	private Bitmap image;
	private Bitmap text;

	private static Bitmap template;
	private static Bitmap highlight;
	private boolean imageAvailable = false;

	private StaticLayout titleLayout;
	private static Rect titleRect;
	private static Rect shadowRect;
	private int width = PREFERRED_WIDTH;
	private int height = PREFERRED_HEIGHT;
	private int paddingLeft=5, paddingTop=5, paddingBottom=5, paddingRight=5;
	private int textPadding=5;

	public ProjectCardView(Context context){
		super(context);
		this.context = context;
		construct();
	}

	/**
	 * @param context
	 */
	public ProjectCardView(Context context, Reference project) {
		this(context);
		this.context = context;
		this.project = project;
		prepareContent();
	}

	public void setProjectReference(Reference project){
		this.project = project;
		prepareContent();
		invalidate();
	}

	public Reference getProjectReference(){
		return project;
	}

	/**
	 * Initial construction of necessary components for drawing this View
	 */
	private void construct(){

		/*
		 * Paint initializiation for text drawing font is Helvetica, bold
		 * 34 point in size, the text color is black. 
		 */
		if(paint==null){
			paint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
			paint.setColor(Color.BLACK);
			paint.setTextSize(28);
			paint.setTypeface(Typeface.create("Helvetica", Typeface.BOLD));
		}

		if(titlePaint==null){
			titlePaint = new Paint();
			titlePaint.setColor(Color.WHITE);
			titlePaint.setAlpha(196);
			titlePaint.setStyle(Style.FILL);
		}

		if(highlightPaint==null){
			highlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			highlightPaint.setAlpha(96);
		}

		if(borderPaint==null){
			borderPaint = new Paint();
			borderPaint.setColor(Color.LTGRAY);
			borderPaint.setStyle(Style.STROKE);
		}

		if(shadowPaint==null){
			shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			shadowPaint.setColor(Color.LTGRAY);
			shadowPaint.setMaskFilter(new BlurMaskFilter(4, Blur.NORMAL));
			shadowPaint.setStyle(Style.FILL_AND_STROKE);
		}

		if(template==null) template = BitmapFactory.decodeResource(getResources(), 
				R.drawable.project_image_template);

		if(highlight==null) highlight = BitmapFactory.decodeResource(getResources(), 
				R.drawable.list_tile_highlight);

		if(titleRect==null)
			titleRect = new Rect(paddingLeft, paddingTop, width-paddingRight, 100);

		if(shadowRect==null)
			shadowRect = new Rect(paddingLeft+1, paddingTop+1, 
					width-paddingRight+1, height-paddingBottom+1);


	}

	/**
	 * Preparation of data to be drawn on screen.
	 */
	private void prepareContent(){

		recycle();
		project.getImageData(context, this);
		int titlePadding = paddingLeft + paddingRight + textPadding * 2;
		titleLayout = new StaticLayout(project.getLabel(), paint, width-titlePadding, 
				Alignment.ALIGN_NORMAL, 1, 1, true);
		text = Bitmap.createBitmap(titleLayout.getWidth(), 
				titleLayout.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas textC = new Canvas(text);
		titleLayout.draw(textC);
	}

	/* (non-Javadoc)
	 * @see android.view.View#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {

		if(isInEditMode()) return;
		canvas.drawRect(shadowRect, shadowPaint);

		if(imageAvailable)
			canvas.drawBitmap(image, paddingLeft, 40, paint);
		else
			canvas.drawBitmap(template, paddingLeft, 40, paint);

		canvas.drawRect(titleRect, titlePaint);
		canvas.drawBitmap(text, paddingLeft + textPadding, 0, paint);
		canvas.drawBitmap(highlight, 0, 0, highlightPaint);
		canvas.drawRect(paddingLeft, paddingTop, width-paddingRight, 
				height-paddingBottom, borderPaint);

	}

	/* (non-Javadoc)
	 * @see android.view.View#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {


		if(isInEditMode()) return;

		int w = resolveSizeAndState(width, widthMeasureSpec, 1);
		int h = resolveSizeAndState(height, heightMeasureSpec, 1);
		setMeasuredDimension(w, h); 
		
	}

	public void recycle(){
		imageAvailable = false;
		image = null;
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

		if(task==null || ((RemoteImageDataLoader)task).getRef()
				.equals(project.getImageRef())){
			image = MediaUtil.createBitmap((byte[]) result, 
					width-paddingLeft-paddingRight);

			imageAvailable = true;
			invalidate();
		}
	}

	/* (non-Javadoc)
	 * @see org.hummer.kickstalker.task.i.TaskCallbackI#onTaskCancelled(org.hummer.kickstalker.task.AbstractTask)
	 */
	@Override
	public void onTaskCancelled(AbstractTask<?, ?, ?> task) {}



}
