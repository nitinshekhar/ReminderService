package com.nitin.job;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

/**
* Properties for a single job.
*/

@Component
public class ReminderJobProperties {

	   private String cronExpression;
	   
	   private String scheduledDate;
	   
	   private String endDate;
	 
	   private String dataToWrite;
	 
	   public String getCronExpression() {
	      return cronExpression;
	   }
	 
	   public void setCronExpression(String cronExpression) {
	      this.cronExpression = cronExpression;
	   }
	 
	   public String getDataToWrite() {
	      return dataToWrite;
	   }
	 
	   public void setDataToWrite(String dataToWrite) {
	      this.dataToWrite = dataToWrite;
	   }

	public String getScheduledDate() {
		return scheduledDate;
	}

	public void setScheduledDate(String scheduledDate) {
		this.scheduledDate = scheduledDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
}
