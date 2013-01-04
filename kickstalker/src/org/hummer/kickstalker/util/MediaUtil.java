/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import org.hummer.kickstalker.R;
import org.hummer.kickstalker.cache.CachedImage;
import org.hummer.kickstalker.cache.ImageCache;
import org.hummer.kickstalker.factory.CacheFactory;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.widget.Toast;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class MediaUtil {

	public static final String TAG = "MEDIAUTIL";

	/**
	 * @param context, Context. The current context.
	 * @param ref, String. The referrer of the remote image.
	 * @param imgCache, ImageCache. An instance of ImageCache.
	 * @return byte[]. The raw byte data of the image.
	 * @throws IOException
	 */
	public static byte[] extractImage(Context context, String ref, ImageCache imgCache) throws IOException {

		if(imgCache.containsKey(ref)) return imgCache.get(ref).getData();

		try {
			URL url = new URL(ref);
			InputStream is = url.openStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();

			for(int b; (b = is.read()) != -1;){
				baos.write(b);
			}

			byte[] returnVal = baos.toByteArray();
			baos.close();
			is.close();

			//cache image
			CachedImage ci = new CachedImage();
			ci.setReference(ref);
			ci.setData(returnVal);
			imgCache.put(ref, ci);
			synchronized(imgCache){
				CacheFactory.store(context, imgCache);
			}
			return returnVal;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return new byte[0];
	}
	
	/**
	 * @param imgdata, byte[]. The raw image byte data.
	 * @param reqWidth, int. The desired width of the image.
	 * @param reqHeight, int. The desired height of the image.
	 * @return Bitmap. The image based on handed in width and height specs.
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
		return Bitmap.createScaledBitmap(bm, reqWidth, reqHeight, true);
		
	}
	
	/**
	 * @param imgdata, byte[]. The raw byte data of the image.
	 * @param reqWidth, int. The desired width of the image.
	 * @return Bitmap. A scaled instance based on handed in width.
	 */
	public static Bitmap createBitmap(byte[] imgdata, int reqWidth){
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeByteArray(imgdata, 0, imgdata.length, options);
		int height = options.outHeight;
		int width = options.outWidth;
		int reqHeight = Math.round(((float)reqWidth) / width * height);
		return createBitmap(imgdata, reqWidth, reqHeight);
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
	
	/**
	 * @param bitmap, Bitmap. The source image to round corners on.
	 * @param pixels, int. The desired radius of the rounded corners.
	 * @return Bitmap. The result with rounded corners.
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
	
	/**
	 * @param context, Context. The current context.
	 * @param intent, Intent. The intent to find Activities for.
	 * @return boolean. True if at least one activity was found for handling the intent.
	 */
	public static boolean isActivityAvailable(Context context, Intent intent) {
	    final PackageManager packageManager = context.getPackageManager();
	    List<ResolveInfo> resolveInfo =
	            packageManager.queryIntentActivities(intent,
	                    PackageManager.MATCH_DEFAULT_ONLY);
	   if (resolveInfo.size() > 0) {
	     return true;
	    }
	   return false;
	}
	
	/**
	 * @param context, Context. The current context.
	 * @param ref, String. The remote referrer for the video stream.
	 */
	public static void streamVideo(Context context, String ref){
		
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse(ref), "video/mp4");
		if(isActivityAvailable(context, i)){ 
			context.startActivity(i);
		} else {
			showToast(context, R.string.error_no_videoplayer, Toast.LENGTH_SHORT);
		}
		
	}
	
	/**
	 * @param context, Context. The current context.
	 * @param msgId, int. The String resource for the message.
	 * @param duration, int. The duration of the toast.
	 */
	public static void showToast(Context context, int msgId, int duration){
		Toast toast = Toast.makeText(context, msgId, duration);
		toast.show();
	}
	
}
