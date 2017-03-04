package com.nitin.job;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import java.util.ArrayList;
import java.util.List;

import org.quartz.CronExpression;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nitin.model.Reminder;
import com.nitin.service.ReminderService;
import com.nitin.util.AppConstant;
import com.nitin.util.ApplicationUtil;

/**
 * Generates a list of JobScheduleModel from the JobScheduleProperties
 */
@Component
public class ReminderJobSchedulerModelGenerator {

	
	private ReminderService reminderService;
	
    @Autowired
    public ReminderJobSchedulerModelGenerator(ReminderService reminderService) {
        this.reminderService = reminderService;
    }
    
    public List<ReminderJobScheduleModel> generateModels() {
//        List<ReminderJobProperties> jobs = jobScheduleProperties.getJobs();
    	List<Reminder> jobs = reminderService.findAll();
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
    
    private ReminderJobScheduleModel generateModelFrom(Reminder job, int jobIndex) {
        JobDetail jobDetail = getJobDetailFor(AppConstant.JOB_NAME + jobIndex, AppConstant.GROUP_NAME, job);
 
//        Trigger trigger = getTriggerFor(job.getCronExpression(), jobDetail);
        Trigger trigger = getTriggerFor(job, jobDetail);
        ReminderJobScheduleModel jobScheduleModel = new ReminderJobScheduleModel(jobDetail, trigger);
        return jobScheduleModel;
    }
    
    private JobDetail getJobDetailFor(String jobName, String groupName, Reminder job) {
        JobDetail jobDetail = JobBuilder.newJob(ReminderJob.class)
                .setJobData(getJobDataMapFrom(job.getReturnURL()))
                .withDescription("Job with data to write : " + job.getReturnURL() +
                        " and CRON expression : " + job.getCronExpression())
                .withIdentity(jobName, groupName)
                .build();
        return jobDetail;
    }
    private JobDataMap getJobDataMapFrom(String dataToWrite) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(AppConstant.DATA_TO_WRITE, dataToWrite);
        return jobDataMap;
    }
    private Trigger getTriggerFor(Reminder job, JobDetail jobDetail) {
//        Trigger trigger = TriggerBuilder.newTrigger()
//                .forJob(jobDetail)
//                .withSchedule(cronSchedule(cronExpression))
//                .build();
    	Trigger trigger = null;

    	if (ApplicationUtil.compareDate(job.getStartDateTime(),job.getEndDateTime()) < 0){
    		if (CronExpression.isValidExpression(job.getCronExpression())) {
    			trigger = TriggerBuilder.newTrigger()
    					.forJob(jobDetail)
    					.withSchedule(cronSchedule(job.getCronExpression()))
    					.startAt(job.getStartDateTime())
    					.endAt(job.getEndDateTime())
    					.withDescription("Cron Scheduled")
    					.build();
    		} else{
    			System.out.println("Invalid Cron Expression");
    			return trigger;
    		}
    	} else {
    		trigger = TriggerBuilder.newTrigger()
    				.forJob(jobDetail)
    				.startAt(job.getStartDateTime())
    				.withSchedule(simpleSchedule()
                	.withMisfireHandlingInstructionFireNow())
    				.withDescription("Single Schedule")
    				.build();
    	}
		System.out.println("Triger time is : "+trigger.getStartTime().toString() +" Scheduled as : "+trigger.getDescription());
        return trigger;
    }
}
