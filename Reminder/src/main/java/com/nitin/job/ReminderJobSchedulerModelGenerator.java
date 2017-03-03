package com.nitin.job;

import java.util.ArrayList;
import java.util.List;

import org.quartz.CronExpression;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.ScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nitin.util.ApplicationUtil;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

/**
 * Generates a list of JobScheduleModel from the JobScheduleProperties
 */
@Component
public class ReminderJobSchedulerModelGenerator {
	private static final String JOB_NAME = "JobName";
	private static final String GROUP_NAME = "Group";
	private static final String DATA_TO_WRITE = "dataToWrite";
	
	private ReminderJobScheduleProperties jobScheduleProperties;
	
    @Autowired
    public ReminderJobSchedulerModelGenerator(ReminderJobScheduleProperties jobScheduleProperties) {
        this.jobScheduleProperties = jobScheduleProperties;
    }
    
    public List<ReminderJobScheduleModel> generateModels() {
        List<ReminderJobProperties> jobs = jobScheduleProperties.getJobs();
        if (jobs.isEmpty()){
        	System.out.println("No Jobs to Run");
        }
        List<ReminderJobScheduleModel> generatedModels = new ArrayList<>();
        for (int i = 0; i < jobs.size(); i++) {
            ReminderJobScheduleModel model = generateModelFrom(jobs.get(i), i);
            generatedModels.add(model);
        }
        return generatedModels;
    }
    
    private ReminderJobScheduleModel generateModelFrom(ReminderJobProperties job, int jobIndex) {
        JobDetail jobDetail = getJobDetailFor(JOB_NAME + jobIndex, GROUP_NAME, job);
 
//        Trigger trigger = getTriggerFor(job.getCronExpression(), jobDetail);
        Trigger trigger = getTriggerFor(job, jobDetail);
        ReminderJobScheduleModel jobScheduleModel = new ReminderJobScheduleModel(jobDetail, trigger);
        return jobScheduleModel;
    }
    
    private JobDetail getJobDetailFor(String jobName, String groupName, ReminderJobProperties job) {
        JobDetail jobDetail = JobBuilder.newJob(ReminderJob.class)
                .setJobData(getJobDataMapFrom(job.getDataToWrite()))
                .withDescription("Job with data to write : " + job.getDataToWrite() +
                        " and CRON expression : " + job.getCronExpression())
                .withIdentity(jobName, groupName)
                .build();
        return jobDetail;
    }
    private JobDataMap getJobDataMapFrom(String dataToWrite) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(DATA_TO_WRITE, dataToWrite);
        return jobDataMap;
    }
    private Trigger getTriggerFor(ReminderJobProperties job, JobDetail jobDetail) {
//        Trigger trigger = TriggerBuilder.newTrigger()
//                .forJob(jobDetail)
//                .withSchedule(cronSchedule(cronExpression))
//                .build();
    	Trigger trigger = null;

    	if (ApplicationUtil.compareDate(ApplicationUtil.convertToDate(job.getScheduledDate()), ApplicationUtil.convertToDate(job.getEndDate())) < 0){
    		if (CronExpression.isValidExpression(job.getCronExpression())) {
    			trigger = TriggerBuilder.newTrigger()
    					.forJob(jobDetail)
    					.withSchedule(cronSchedule(job.getCronExpression()))
    					.startAt(ApplicationUtil.convertToDate(job.getScheduledDate()))
    					.endAt(ApplicationUtil.convertToDate(job.getEndDate()))
    					.withDescription("Cron Scheduled")
    					.build();
    		} else{
    			System.out.println("Invalid Cron Expression");
    			return trigger;
    		}
    	} else {
    		trigger = TriggerBuilder.newTrigger()
    				.forJob(jobDetail)
    				.startAt(ApplicationUtil.convertToDate(job.getScheduledDate()))
    				.withSchedule(simpleSchedule()
                	.withMisfireHandlingInstructionFireNow())
    				.withDescription("Single Schedule")
    				.build();
    	}
		System.out.println("Triger time is : "+trigger.getStartTime().toString() +" Scheduled as : "+trigger.getDescription());
        return trigger;
    }
}
