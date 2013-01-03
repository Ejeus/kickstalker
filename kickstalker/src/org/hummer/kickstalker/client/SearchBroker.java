/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.client;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import org.hummer.kickstalker.data.SearchResult;
import org.hummer.kickstalker.data.SearchResult.ProjectResult;
import org.hummer.kickstalker.http.KickstarterResources;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.gson.Gson;

import android.util.Log;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class SearchBroker {

	public static final String TAG = "SRCBRKR";
	private Gson gson;
	private String searchURL;
	
	
	public SearchBroker(){
		gson = new Gson();
		searchURL = KickstarterClient.BASE_URL + KickstarterResources.PAGE_SEARCH;
	}
	
	public Document search(String term){
		
		String compositeHTML="";
		
		try {
			String json = Jsoup.connect(searchURL + URLEncoder.encode(term, "UTF-8"))
					.ignoreContentType(true).execute().body();
			SearchResult result = gson.fromJson(json, SearchResult.class);
			
			List<ProjectResult> projects = result.getProjects();
			for(int i=0;i<projects.size();i++)
				compositeHTML += projects.get(i).getCard_html();
		} catch (IOException e) {
			Log.e(TAG, e.getLocalizedMessage());
		}
		
		return Jsoup.parse(compositeHTML);
		
	}
	
}
