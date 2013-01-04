/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.view;

import org.hummer.kickstalker.R;
import org.hummer.kickstalker.util.MediaUtil;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * An ImageView implementation that shows a "play" button on top,
 * if a click hanlder is attached to it.
 * 
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class VideoPlayerImageView extends ImageView {

	static final int PLAYERICON_SIZE = 256;
	static final int ALPHA = 82;
	private static Paint translucentPaint;
	private static Bitmap playerIcon;
	int playerIconX, playerIconY;
	boolean video = false;
	

	public VideoPlayerImageView(Context context) {
		super(context);
		init();
	}

	public VideoPlayerImageView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	public VideoPlayerImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	/**
	 * Initialize components necessary for drawing.
	 */
	public void init(){

		if(translucentPaint==null){
			translucentPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
			translucentPaint.setAlpha(ALPHA);
		}

		if(playerIcon==null){
			playerIcon = BitmapFactory.decodeResource(getResources(), 
					R.drawable.videoplayer_play);
			playerIcon = MediaUtil.scaleImage(playerIcon, 
					PLAYERICON_SIZE, PLAYERICON_SIZE);
		}

	}

	/* (non-Javadoc)
	 * @see android.widget.ImageView#onDraw(android.graphics.Canvas)
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		if(video && playerIcon!=null)
			canvas.drawBitmap(playerIcon, 
					playerIconX, playerIconY, translucentPaint);

	}


	/* (non-Javadoc)
	 * @see android.widget.ImageView#onMeasure(int, int)
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	/**
	 * Adjusts the play icon to width and heigh of the underlying picture.
	 * @param bm, Bitmap. The new image content to draw.
	 */
	private void scale(Bitmap bm){
		int w = bm.getWidth();
		int h = bm.getHeight();
		if(playerIcon==null || w <= 0 || h <= 0) return;


		playerIconX = (w-PLAYERICON_SIZE)/2;
		playerIconY = (h-PLAYERICON_SIZE)/2;
	}

	/* (non-Javadoc)
	 * @see android.widget.ImageView#setImageBitmap(android.graphics.Bitmap)
	 */
	@Override
	public void setImageBitmap(Bitmap bm) {
		scale(bm);
		super.setImageBitmap(bm);
	}

	/* (non-Javadoc)
	 * @see android.view.View#setOnClickListener(android.view.View.OnClickListener)
	 */
	@Override
	public void setOnClickListener(OnClickListener l) {
		super.setOnClickListener(l);
		video = l!=null;
		invalidate();
	}



}
