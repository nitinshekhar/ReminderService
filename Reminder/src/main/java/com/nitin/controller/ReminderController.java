package com.nitin.controller;

import java.sql.Date;
import java.text.SimpleDateFormat;

import java.util.List;

import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.nitin.config.QuartzConfiguration;
import com.nitin.job.ReminderJob;
import com.nitin.model.Reminder;
import com.nitin.repository.ReminderRepository;

@RestController
public class ReminderController {

	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
	private Logger log = LoggerFactory.getLogger(ReminderController.class);

	@Autowired
	private ReminderRepository reminderRepository;
	
	@Autowired
	private ReminderJob reminderJob;

	@Value("http://localhost:8181/test")
	String conKey2;

	@RequestMapping("/registerReminder")
	public @ResponseBody String greeting(@RequestParam(value = "AppName", defaultValue = "Test Application") String AppName,
			@RequestParam(defaultValue = "www.google.ca") String returnURL,
			@RequestParam(defaultValue = "02/25/2017 18:50") String reminderSchedule,
			@RequestParam(defaultValue = "false") String reoccurance,
			@RequestParam(defaultValue = "System") String createdBy) {
		log.info("Registering the Reminder Service from Application" + AppName + "." + "return URL : " + returnURL
				+ " , " + "Scheduled Date : " + reminderSchedule);
		Date sqlDate = null;
		String id = "";
		try {
			sqlDate = new Date(simpleDateFormat.parse(reminderSchedule).getTime());

			Reminder reminder = new Reminder(AppName, returnURL, sqlDate, Boolean.parseBoolean(reoccurance), createdBy);
			reminderRepository.save(reminder);
//			scheduleJob(reminder);
//			JobDetail reminderJob = JobBuilder.newJob(ReminderJob.class).withIdentity("reminderjob","reminderjobgroup").storeDurably().build();
//			SimpleTrigger trigger = (SimpleTrigger) TriggerBuilder.newTrigger().withIdentity("reminderTrigger","reminderTriggerGroup").startAt(sqlDate).build();
			
//			reminderJob.getJobDataMap().put("returnURL", returnURL);
			JobDataMap dataMap = new JobDataMap();
			dataMap.put("reminder", reminder);
//			JobDetailFactoryBean factoryBean = reminderJob.sampleJob();
//			SimpleTriggerFactoryBean trigger = QuartzConfiguration.createTrigger(factoryBean.getObject());

			id = String.valueOf(reminder.getId());
		} catch (Exception e) {
			log.info("Error converting the Date :" + e.getMessage());
		}
		return "Reminder succesfully created with id = " + id;
	}

	@RequestMapping("/deregisterReminder")
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

	
	/*
	private void scheduleJob(Reminder reminder) {
		String reminderId = reminder.getId().toString();

		JobDetail job = JobBuilder.newJob(ReminderJob.class).withIdentity("Reminder_J_" + reminderId).build();

		Trigger trigger = TriggerBuilder.newTrigger().forJob(job).startAt(reminder.getScheduleDateTime()).build();
		try {
			scheduler.scheduleJob(job, trigger);
		} catch (SchedulerException e) {
			System.out.println("Unable to schedule the Job");
		}
	}*/
}
