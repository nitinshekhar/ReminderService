package com.nitin.job;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;

public class ReminderJobListener implements JobListener{
	
	private Logger log = Logger.getLogger(ReminderJobListener.class);
	@Override
	public String getName() {
		return "Reminder Job Listener";
	}

	@Override
	public void jobToBeExecuted(JobExecutionContext context) {
	       log.info("jobToBeExecuted : " + context.getJobDetail().getDescription() +" on "+new Date());	
	}

	@Override
	public void jobExecutionVetoed(JobExecutionContext context) {
		log.info("jobExecutionVetoed : " + context.getJobDetail().getDescription() +" on "+new Date());
	}

	@Override
	public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
		log.info("jobwasExecuted : " + context.getJobDetail().getDescription() +" on "+new Date());
	}

}
