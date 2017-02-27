package com.nitin.job;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.stereotype.Component;

/**
* Properties for a single job.
*/

@Component
public class ReminderJobProperties {
	   @NotNull
	   @NotEmpty
	   private String cronExpression;
	 
	   @NotNull
	   @NotEmpty
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
}
