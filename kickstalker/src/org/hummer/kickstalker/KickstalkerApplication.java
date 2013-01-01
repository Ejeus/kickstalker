/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker;

import android.app.Application;
import android.content.Context;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class KickstalkerApplication extends Application {

	private static Context context;

    public void onCreate(){
        super.onCreate();
        KickstalkerApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return KickstalkerApplication.context;
    }
    
}
