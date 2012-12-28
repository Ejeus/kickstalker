/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.view;

import org.hummer.kickstalker.R;
import org.hummer.kickstalker.data.Reference;

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
import android.graphics.RectF;
import android.graphics.Typeface;
import android.text.Layout.Alignment;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import android.view.View;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class ProjectCardView extends View {
	
	static final String TAG = "PRJCDVW";
	static final int PREFERRED_WIDTH = 355;
	static final int PREFERRED_HEIGHT = 305;
	Reference project;
	TextPaint paint;
	Paint titlePaint;
	Paint highlightPaint;
	Paint borderPaint;
	Paint shadowPaint;
	Bitmap image;
	Bitmap text;
	Bitmap highlight;
	RectF imgScaleTarget;
	private StaticLayout titleLayout;
	private Rect titleRect;
	private Rect shadowRect;
	int width = PREFERRED_WIDTH;
	int height = PREFERRED_HEIGHT;
	int paddingLeft=5, paddingTop=5, paddingBottom=5, paddingRight=5;
	int textPadding=5;
	
	public ProjectCardView(Context context){
		super(context);
		construct();
	}
	
	/**
	 * @param context
	 */
	public ProjectCardView(Context context, Reference project) {
		this(context);
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
	
	private void construct(){
		
		/*
		 * Paint initializiation for text drawing font is Helvetica, bold
		 * 34 point in size, the text color is black. 
		 */
		paint = new TextPaint(TextPaint.ANTI_ALIAS_FLAG);
		paint.setColor(Color.BLACK);
		paint.setTextSize(28);
		paint.setTypeface(Typeface.create("Helvetica", Typeface.BOLD));
		
		titlePaint = new Paint();
		titlePaint.setColor(Color.WHITE);
		titlePaint.setAlpha(196);
		titlePaint.setStyle(Style.FILL);
		
		highlightPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		highlightPaint.setAlpha(96);
		
		borderPaint = new Paint();
		borderPaint.setColor(Color.LTGRAY);
		borderPaint.setStyle(Style.STROKE);
		
		shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		shadowPaint.setColor(Color.LTGRAY);
		shadowPaint.setMaskFilter(new BlurMaskFilter(4, Blur.NORMAL));
		shadowPaint.setStyle(Style.FILL_AND_STROKE);
		
		highlight = BitmapFactory.decodeResource(getResources(), 
				R.drawable.list_tile_highlight);
		
		titleRect = new Rect(paddingLeft, paddingTop, width-paddingRight, 100);
		shadowRect = new Rect(paddingLeft+1, paddingTop+1, 
				width-paddingRight+1, height-paddingBottom+1);
		imgScaleTarget = new RectF(paddingLeft, 40, width-paddingRight, 300);
		
	}
	
	private void prepareContent(){
		byte[] imgdata = project.getImageData();
		image = BitmapFactory.decodeByteArray(imgdata, 0, imgdata.length);
		
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
		canvas.drawBitmap(image, null, imgScaleTarget, paint);
		
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
		Log.i(TAG, "Width:" + MeasureSpec.getSize(widthMeasureSpec) + 
				" | " + MeasureSpec.getSize(heightMeasureSpec));
		
		int w = resolveSizeAndState(width, widthMeasureSpec, 1);
		int h = resolveSizeAndState(height, heightMeasureSpec, 1);
		setMeasuredDimension(w, h);
		
	}
	
	

}
