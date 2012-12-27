/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class MediaUtil {

	/**
	 * @return Bitmap - A scaled image fitting the details image panel.
	 */
	public static Bitmap scaleImage(Bitmap bm, int tw, int th) {
		
		float factor = ((float)bm.getWidth() / tw);
		
		Paint p = new Paint(Paint.ANTI_ALIAS_FLAG);
		Bitmap scaled = Bitmap.createBitmap(tw, th, Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(scaled);
		Rect source = new Rect(0, 0, bm.getWidth(), 
				Float.valueOf(th * factor).intValue());
		Rect dest = new Rect(0, 0, tw, th);
		
		c.drawBitmap(bm, source, dest, p);
		
		return scaled;
		
	}
	
	public static void streamVideo(Context context, String ref){
		
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse(ref), "video/mp4");
		context.startActivity(i);
		
	}
	
}