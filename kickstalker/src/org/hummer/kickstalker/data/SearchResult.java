/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.data;

import java.io.Serializable;
import java.util.List;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class SearchResult implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1548376946809636610L;
	List<ProjectResult> projects;
	String[] categories;
	String[] locations;
	String header;
	

	public List<ProjectResult> getProjects() {
		return projects;
	}


	public void setProjects(List<ProjectResult> projects) {
		this.projects = projects;
	}


	public String[] getCategories() {
		return categories;
	}


	public void setCategories(String[] categories) {
		this.categories = categories;
	}


	public String[] getLocations() {
		return locations;
	}


	public void setLocations(String[] locations) {
		this.locations = locations;
	}


	public String getHeader() {
		return header;
	}


	public void setHeader(String header) {
		this.header = header;
	}


	public static class ProjectResult {
		String name;
		String id;
		String card_html;
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getCard_html() {
			return card_html;
		}
		public void setCard_html(String card_html) {
			this.card_html = card_html;
		}
	}
	
}
