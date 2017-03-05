package com.nitin.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity

public class Reminder implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long reminderId;
	private String applicationName;
	private Date startDateTime;
	private Date endDateTime;
	private String cronExpression;
	private String returnURL;
	private String createdBy;
	private Date createdOn;
	
	public Reminder() { }
	
	public Reminder(long id){
		this.reminderId = id;
	}
	
	public Reminder(String appName, Date startDateTime, Date endDateTime, String cronExpression, String returnURL, String createdBy){
		this.applicationName = appName;
		this.returnURL = returnURL;
		this.startDateTime = startDateTime;
		this.endDateTime = endDateTime;
		this.cronExpression = cronExpression;
		this.createdBy = createdBy;
		this.createdOn =  new Date(Calendar.getInstance().getTime().getTime());
	}

	public long getReminderId() {
		return reminderId;
	}

	public void setReminderId(long reminderId) {
		this.reminderId = reminderId;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	public Date getStartDateTime() {
		return startDateTime;
	}

	public void setStartDateTime(Date startDateTime) {
		this.startDateTime = startDateTime;
	}

	public Date getEndDateTime() {
		return endDateTime;
	}

	public void setEndDateTime(Date endDateTime) {
		this.endDateTime = endDateTime;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public String getReturnURL() {
		return returnURL;
	}

	public void setReturnURL(String returnURL) {
		this.returnURL = returnURL;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}



}
