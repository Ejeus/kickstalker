/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.view;

import org.hummer.kickstalker.R;
import org.hummer.kickstalker.util.MathUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TabHost;

public class FlingableTabHost extends TabHost {
	public static final String TAG = "FLINGTABHOST";
	GestureDetector mGestureDetector;

	Animation mriAnim;
	Animation mroAnim;
	Animation mliAnim;
	Animation mloAnim;

	public FlingableTabHost(Context context, AttributeSet attrs) {
		super(context, attrs);

		mriAnim = AnimationUtils.loadAnimation(context, R.anim.slide_right_in);
		mroAnim = AnimationUtils.loadAnimation(context, R.anim.slide_right_out);
		mliAnim = AnimationUtils.loadAnimation(context, R.anim.slide_left_in);
		mloAnim = AnimationUtils.loadAnimation(context, R.anim.slide_left_out);
		
		if(isInEditMode()) return;
		
		final int minScaledFlingVelocity = ViewConfiguration.get(context)
				.getScaledMinimumFlingVelocity() * 5; 

		mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
			
			/* (non-Javadoc)
			 * @see android.view.GestureDetector.SimpleOnGestureListener#onFling(
			 * 	android.view.MotionEvent, android.view.MotionEvent, float, float)
			 */
			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
					float velocityY) {
				
				int tabCount = getTabWidget().getTabCount();
				int currTab = getCurrentTab();
				if (Math.abs(velocityX) > minScaledFlingVelocity &&
						Math.abs(velocityY) < minScaledFlingVelocity) {

					final boolean right = velocityX < 0;
					final int newTab = MathUtil.constrain(currTab + (right ? 1 : -1),
							0, tabCount - 1);
					if (newTab != currTab) {

						View currView = getCurrentView();
						setCurrentTab(newTab);
						View newView = getCurrentView();

						currView.startAnimation(
								right ? mloAnim : mroAnim);
						newView.startAnimation(right ? mriAnim : mliAnim);
						
					}
				}
				return super.onFling(e1, e2, velocityX, velocityY);
			}
			
		});
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (mGestureDetector.onTouchEvent(ev)) {
			return true;
		}
		return super.onInterceptTouchEvent(ev);
	}
	
}
