package com.nitin.controller;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import org.quartz.CronExpression;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nitin.job.ReminderJob;
import com.nitin.model.Reminder;
import com.nitin.repository.ReminderRepository;
import com.nitin.util.AppConstant;
import com.nitin.util.ApplicationUtil;

@RestController
public class ReminderServiceController {

	private Logger log = LoggerFactory.getLogger(ReminderServiceController.class);

	@Autowired
	private ReminderRepository reminderRepository;


	@RequestMapping("/registerReminder")
	public @ResponseBody String greeting(@RequestParam(value = "AppName", defaultValue = "Test Application") String AppName,
			@RequestParam(value = "StartDateTime", defaultValue = "2017/03/04 11:03:00") String startDateTime,
			@RequestParam(value = "EndDateTime", defaultValue = "2017/03/04 11:03:00") String endDateTime,
			@RequestParam(value = "Cron", defaultValue = "0 * * * * * ?") String cronExpression,
			@RequestParam(value = "ReturnURL", defaultValue = "http://localhost:8181/test") String returnURL,
			@RequestParam(value = "CreatedBy", defaultValue = "System") String createdBy) {
		log.info("Registering the Reminder Service from Application" + AppName + "." + "return URL : " + returnURL
				+ " , " + "Start Date : " + startDateTime);

		Reminder reminder = new Reminder(AppName, ApplicationUtil.convertToDate(startDateTime), ApplicationUtil.convertToDate(endDateTime), cronExpression, returnURL, createdBy);
		reminderRepository.save(reminder);
		scheduleJob(reminder);
//			JobDetail reminderJob = JobBuilder.newJob(ReminderJob.class).withIdentity("reminderjob","reminderjobgroup").storeDurably().build();
//			SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity("reminderTrigger","reminderTriggerGroup").startAt(sqlDate).build();
			
//			reminderJob.getJobDataMap().put("returnURL", returnURL);
//			JobDataMap dataMap = new JobDataMap();
//			dataMap.put("reminder", reminder);
//			JobDetailFactoryBean factoryBean = reminderJob.sampleJob();
//			SimpleTriggerFactoryBean trigger = QuartzConfiguration.createTrigger(factoryBean.getObject());

		return "Reminder succesfully created with id = " + String.valueOf(reminder.getReminderId());
	}

	@RequestMapping(value = "/deregisterReminder")
	@ResponseBody
	public String delete(@RequestParam long id) {
		try {
			Reminder reminder = new Reminder(id);
			reminderRepository.delete(reminder);
		} catch (Exception ex) {
			return "Error deleting the reminder:" + ex.toString();
		}
		return "Reminder succesfully deregistered!";
	}

	@RequestMapping("/listReminder")
	public List<Reminder> getReminder() {
		return reminderRepository.findAll();
	}

	
	
	private void scheduleJob(Reminder job) {
        Trigger trigger = null;
		JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(AppConstant.DATA_TO_WRITE, job.getReturnURL());
        JobDetail jobDetail = JobBuilder.newJob(ReminderJob.class)
                .setJobData(jobDataMap)
                .withDescription("Job with data to write : " + job.getReturnURL() +
                        " and CRON expression : " + job.getCronExpression())
                .withIdentity(AppConstant.JOB_NAME+job.getReminderId(), AppConstant.GROUP_NAME)
                .build();
		
    	if (ApplicationUtil.compareDate(job.getStartDateTime(),job.getEndDateTime()) < 0){
    		if (CronExpression.isValidExpression(job.getCronExpression())) {
    			System.out.println("Cron Scheduling the job : "+ job.getReminderId());
    			trigger = TriggerBuilder.newTrigger()
    					.forJob(jobDetail)
    					.withSchedule(cronSchedule(job.getCronExpression()))
    					.startAt(job.getStartDateTime())
    					.endAt(job.getEndDateTime())
    					.withDescription("Cron Scheduled")
    					.build();
    		} else{
    			System.out.println("Invalid Cron Expression");
    			return;
    		}
    	} else {
			System.out.println("Single Scheduling the job : "+ job.getReminderId());
    		trigger = TriggerBuilder.newTrigger()
    				.forJob(jobDetail)
    				.startAt(job.getStartDateTime())
    				.withSchedule(simpleSchedule()
                	.withMisfireHandlingInstructionFireNow())
    				.withDescription("Single Schedule")
    				.build();
    	}
    	QuartzScheduler.addJobs(jobDetail, trigger);
	}
}
