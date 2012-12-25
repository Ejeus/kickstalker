/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.hummer.kickstalker.data.Reference;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
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
	private List<Reference> projects;
	private List<ProjectCardView> tiles;
	private Map<String, Bitmap> cache;
	private Paint paint;
	private int singleTileWidth;
	private int singleTileHeight;
	private Set<OnItemSelectedListener> listeners;
	
	private float x, y;
	private float movedX, movedY;
	private float threshold=3;
	private boolean click;

	public ProjectCardListView(Context context){
		super(context);
		init();
	}
	
	/**
	 * @param context
	 */
	public ProjectCardListView(Context context, List<Reference> projects) {
		super(context);
		this.projects = projects;
		tiles = new ArrayList<ProjectCardView>();
		cache = new HashMap<String, Bitmap>();

		for(int i=0, l=projects.size(); i<l; i++){
				ProjectCardView v = new ProjectCardView(context,projects.get(i));
				
				tiles.add(v);
		}
		init();

	}
	
	/**
	 * Needs to be called on creation
	 */
	private void init(){
		
		listeners = new HashSet<ProjectCardListView.OnItemSelectedListener>();
		paint = new Paint(Paint.ANTI_ALIAS_FLAG);
		
	}

	/**
	 * @param l, OnItemSelectedListener - The listener to add
	 */
	public void addItemSelectedListener(OnItemSelectedListener l){
		listeners.add(l);
	}
	
	/**
	 * @param l, OnItemSelectedListener - The listener to remove
	 */
	public void removeItemSelectedListener(OnItemSelectedListener l){
		listeners.remove(l);
	}
	
	/**
	 * @param canvas, Canvas - The canvas to draw component on.
	 */
	@Override
	protected void onDraw(Canvas canvas) {
		if(isInEditMode()) return;
		int x=0,y=0;
		for(int i=0, l=tiles.size(); i<l; i++){
			
			Reference prj = projects.get(i);
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

	/* (non-Javadoc)
	 * @see android.view.View#onTouchEvent(android.view.MotionEvent)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		
		switch(event.getAction()){
		
		case MotionEvent.ACTION_DOWN:
			x=event.getX();
			y=event.getY();
			click = true;
			return true;
		case MotionEvent.ACTION_MOVE:
			movedX = event.getX();
			movedY = event.getY();
		case MotionEvent.ACTION_UP:
			if(movedX>=x-threshold && movedX<=x+threshold &&
					movedY>=y-threshold && movedY<=y+threshold && click){
				ProjectCardView tile = findTile(x, y);
				for(OnItemSelectedListener l : listeners)
					l.onSelected(this, tile);
				click=false;
			}
		default:
			return false;
		}
		
	}	

	/**
	 * @param x, double - The x coordinate of the point to find tile at.
	 * @param y, double - The y coordinate of the point to find tile at.
	 * @return ProjectCardView - The found tile at this position.
	 */
	private ProjectCardView findTile(double x, double y){
		double wx = Math.floor(x / singleTileWidth);
		double wy = Math.floor(y / singleTileHeight);
		
		int idx = Double.valueOf(wy * 2 + wx).intValue();
		return tiles.get(idx);
	}
	
	public interface OnItemSelectedListener{
		
		public void onSelected(ProjectCardListView view,
				ProjectCardView select);
		
	}
	
}
