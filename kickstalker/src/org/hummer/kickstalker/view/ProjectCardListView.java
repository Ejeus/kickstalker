/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hummer.kickstalker.data.Project;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class ProjectCardListView extends View {
	
	public static final String TAG = "PRJCDLST";
	public static final int TILE_HEIGHT_STANDARD = 305;
	private List<Project> projects;
	private List<ProjectCardView> tiles;
	private Map<String, Bitmap> cache;
	private Paint paint;
	private int singleTileWidth;
	private int singleTileHeight;
	
	private float x, y;
	private float movedX, movedY;
	private float threshold=10;

	public ProjectCardListView(Context context){
		super(context);
	}
	
	/**
	 * @param context
	 */
	public ProjectCardListView(Context context, List<Project> projects) {
		super(context);
		this.projects = projects;
		tiles = new ArrayList<ProjectCardView>();
		cache = new HashMap<String, Bitmap>();
		
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);

		for(int i=0, l=projects.size(); i<l; i++)
				tiles.add(new ProjectCardView(context,projects.get(i)));

	}

	/**
	 * @param canvas, Canvas - The canvas to draw component on.
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		if(isInEditMode()) return;
		int x=0,y=0;
		for(int i=0, l=tiles.size(); i<l; i++){
			
			Project prj = projects.get(i);
			ProjectCardView tile = tiles.get(i);
			int tileIdx = i % 2;
			x = singleTileWidth * tileIdx;
			
			drawTile(canvas, tile, prj.getRef(), x, y);
			
			if(tileIdx==1) y += singleTileHeight;
		}

	}

	/**
	 * @param canvas, Canvas - The canvas to use for drawing.
	 * @param tile, ProjectCardView - The tile to draw
	 * @param cacheKey, String - The key to look for in cache
	 * @param x, int - The left position to draw from
	 * @param y, int - The top position to draw from
	 */
	private void drawTile(Canvas canvas, ProjectCardView tile, 
			String cacheKey, int x, int y){
		
		if(tile.isDirty() || !cache.containsKey(cacheKey)){
			Bitmap bm = Bitmap.createBitmap(singleTileWidth, singleTileHeight, 
					Bitmap.Config.ARGB_8888);
			Canvas c = new Canvas(bm);

			tile.measure(singleTileWidth, singleTileHeight);
			tile.draw(c);
			cache.put(cacheKey, bm);
		} 

		canvas.drawBitmap(cache.get(cacheKey), x,  y, paint);

	}

	/**
	 * @param widthMeasureSpec, int - The preferred width
	 * @param heightMeasureSpec, int - The preferred height
	 */
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = MeasureSpec.getSize(heightMeasureSpec);
		singleTileWidth = width / 2;
		singleTileHeight = TILE_HEIGHT_STANDARD;
		
		if(isInEditMode()) return;
		double lines = projects.size() / 2;
		double minlines = Math.floor(lines);

		if(minlines < lines) lines++;
		
		height = Double.valueOf(lines).intValue() * singleTileHeight;
		int w = resolveSizeAndState(width, widthMeasureSpec, 1);
		int h = resolveSizeAndState(height, heightMeasureSpec, 0);
		
		setMeasuredDimension(w, h);
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		switch(event.getAction()){
		
		case MotionEvent.ACTION_DOWN:
			x=event.getX();
			y=event.getY();
			return true;
		case MotionEvent.ACTION_MOVE:
			movedX = event.getX();
			movedY = event.getY();
		case MotionEvent.ACTION_UP:
			if(movedX>=x-threshold && movedX<=x+threshold &&
					movedY>=y-threshold && movedY<=y+threshold){
				Log.i(TAG, findTile(x, y).getProject().getTitle());
			}
		default:
			return false;
		}
		
	}	

	private ProjectCardView findTile(double x, double y){
		double wx = Math.floor(x / singleTileWidth);
		double wy = Math.floor(y / singleTileHeight);
		
		int idx = Double.valueOf(wy * 2 + wx).intValue();
		return tiles.get(idx);
	}
	
}
