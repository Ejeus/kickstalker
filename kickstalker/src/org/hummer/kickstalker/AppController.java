/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker;

import java.io.IOException;
import java.io.StreamCorruptedException;

import org.hummer.kickstalker.client.KickstarterClient;
import org.hummer.kickstalker.factory.CacheFactory;

import android.content.Context;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class AppController {

	public static final String SETTINGS_CREDENTIALS = "SETTINGS_CREDENTIALS";
	private static AppController INSTANCE;
	private ProjectCache projectCache;
	private KickstarterClient client;
	
	private AppController(){}
	
	public static AppController getInstance(){
		INSTANCE = INSTANCE == null ? new AppController() : INSTANCE;
		return INSTANCE;
	}
	
	public KickstarterClient getClient(){
		if(client==null) client = new KickstarterClient();
		return client;
	}
	
	public ProjectCache getProjectCache(Context context){
		if(projectCache==null)
			try {
				projectCache = CacheFactory.loadProjectCache(context);
			} catch (StreamCorruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		
		return projectCache;
	}

}
