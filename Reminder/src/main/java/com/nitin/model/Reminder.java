package com.nitin.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="Reminders")
public class Reminder implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long reminderId;
	@NotNull
	private String applicationName;
	@NotNull
	private String returnURL;
	@NotNull
	private Date scheduleDateTime;
	@NotNull
	private boolean reoccurance;


	private String createdBy;
	private Date createdOn;
	
	public Reminder() { }
	
	public Reminder(long id){
		this.reminderId = id;
	}
	
	public Reminder(String appName, String returnURL, Date scheduleDateTime, boolean reoccurance, String createdBy){
		this.applicationName = appName;
		this.returnURL = returnURL;
		this.scheduleDateTime = scheduleDateTime;
		this.reoccurance = reoccurance;
		this.createdBy = createdBy;
		this.createdOn =  new Date(Calendar.getInstance().getTime().getTime());
	}
	public boolean isReoccurance() {
		return reoccurance;
	}

	public void setReoccurance(boolean reoccurance) {
		this.reoccurance = reoccurance;
	}	
	public Long getId() {
		return reminderId;
	}
	public String getApplicationName() {
		return applicationName;
	}
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}
	public String getReturnURL() {
		return returnURL;
	}
	public void setReturnURL(String returnUR) {
		this.returnURL = returnUR;
	}
	public Date getScheduleDateTime() {
		return scheduleDateTime;
	}
	public void setScheduleDateTime(Date scheduleDateTime) {
		this.scheduleDateTime = scheduleDateTime;
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
