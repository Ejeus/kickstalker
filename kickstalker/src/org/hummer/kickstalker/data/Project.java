/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.data;

import java.io.Serializable;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class Project extends AbstractData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5890355485977036023L;
	
	private String ref;
	private String title;
	private String shortDescription;
	private String description;
	private byte[] image;
	private boolean videoAvailable;
	private Reference video;
	private int backers;
	private int pledged;
	private float percent;
	private int goal;
	private int timeLeft;
	private String owner;
	
	public Project(String ref){
		super();
		this.ref = ref;
	}
	public String getRef(){
		return ref;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public byte[] getImageData(){
		return image;
	}
	public void setImageData(byte[] image){
		this.image = image;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getShortDescription() {
		return shortDescription;
	}
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}
	public boolean hasVideo() {
		return videoAvailable;
	}
	public void setHasVideo(boolean hasVideo) {
		videoAvailable = hasVideo;
	}
	public Reference getVideoReference() {
		return video;
	}
	public void setVideoReference(Reference video) {
		this.video = video;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}
	public int getBackers() {
		return backers;
	}
	public void setBackers(int backers) {
		this.backers = backers;
	}
	public int getPledged() {
		return pledged;
	}
	public void setPledged(int pledged) {
		this.pledged = pledged;
	}
	public int getGoal() {
		return goal;
	}
	public void setGoal(int goal) {
		this.goal = goal;
	}
	public int getTimeLeft() {
		return timeLeft;
	}
	public void setTimeLeft(int timeLeft) {
		this.timeLeft = timeLeft;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public float getPercent() {
		return percent;
	}
	public void setPercent(float percent) {
		this.percent = percent;
	}
}
