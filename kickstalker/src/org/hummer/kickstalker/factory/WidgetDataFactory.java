/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.factory;

import java.io.IOException;
import java.io.StreamCorruptedException;

import org.hummer.kickstalker.widget.WidgetDataMap;

import android.content.Context;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class WidgetDataFactory {

	static final String FILENAME_WIDGETDATA = "widget.data";
	
	public static void store(Context context, WidgetDataMap wdm) throws IOException{
		BaseFactory.store(context, wdm, FILENAME_WIDGETDATA);
	}
	
	public static WidgetDataMap load(Context context) throws 
	StreamCorruptedException, IOException, ClassNotFoundException{
		return (WidgetDataMap) BaseFactory.load(context, FILENAME_WIDGETDATA);
	}
	
}
