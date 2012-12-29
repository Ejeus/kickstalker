/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.Toast;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class MediaUtil {

	/**
	 * @param imgdata
	 * @return
	 */
	public static Bitmap createBitmap(byte[] imgdata, int reqWidth, int reqHeight) {
		
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(imgdata, 0, imgdata.length, options);
		int height = options.outHeight;
		int width = options.outWidth;
		if(reqHeight==0) reqHeight = height * reqWidth / width;
		int inSampleSize = 1;
		
		if (height > reqHeight || width > reqWidth) {
	        if (width > height) {
	            inSampleSize = Math.round((float)height / (float)reqHeight);
	        } else {
	            inSampleSize = Math.round((float)width / (float)reqWidth);
	        }
	    }
		
		options.inSampleSize = inSampleSize;
		options.inJustDecodeBounds = false;
		
		Bitmap bm = BitmapFactory.decodeByteArray(imgdata, 0, imgdata.length, options);
		if(inSampleSize==1)
			return Bitmap.createScaledBitmap(bm, reqWidth, reqHeight, true);
		else
			return bm;
		
	}
	
	/**
	 * @return Bitmap - A scaled image fitting the details image panel.
	 */
	public static Bitmap scaleImage(Bitmap bm, int tw) {
		
		int th = bm.getHeight() / tw * bm.getWidth();
		return Bitmap.createScaledBitmap(bm, tw, th, true);
		
	}
	
	/**
	 * @return Bitmap - A scaled image fitting the details image panel.
	 */
	public static Bitmap scaleImage(Bitmap bm, int tw, int th) {
		
		return Bitmap.createScaledBitmap(bm, tw, th, true);
		
	}
	
	public static void streamVideo(Context context, String ref){
		
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse(ref), "video/mp4");
		context.startActivity(i);
		
	}
	
	public static void showToast(Context context, int msgId, int duration){
		Toast toast = Toast.makeText(context, msgId, duration);
		toast.show();
	}
	
}
