package com.nitin.controller;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;

import com.nitin.job.ReminderJobScheduleModel;
import com.nitin.job.ReminderJobSchedulerModelGenerator;

/**
 * Scheduler to schedule and start the configured jobs
 */
@Component
public class QuartzScheduler {

    private static SchedulerFactoryBean schedulerFactoryBean;
    private ReminderJobSchedulerModelGenerator jobSchedulerModelGenerator;

    @Autowired
    public QuartzScheduler(SchedulerFactoryBean schedulerFactoryBean, ReminderJobSchedulerModelGenerator jobSchedulerModelGenerator) {
        this.schedulerFactoryBean = schedulerFactoryBean;
        this.jobSchedulerModelGenerator = jobSchedulerModelGenerator;
    }
    @PostConstruct
    public void init() {
        scheduleJobs();
    }
    
    public void scheduleJobs() {
        Scheduler scheduler = schedulerFactoryBean.getScheduler();
        List<ReminderJobScheduleModel> jobScheduleModels = jobSchedulerModelGenerator.generateModels();
        for (ReminderJobScheduleModel model : jobScheduleModels) {
            try {
                Date fireDate = scheduler.scheduleJob(model.getJobDetail(), model.getTrigger());
                System.out.println("job : "+model.getJobDetail().getKey() + "Scheduled will fire at "+fireDate);
            } catch (SchedulerException e) {
                // log the error
            }
        }
        try {
            scheduler.start();
        } catch (SchedulerException e) {
            // log the error
        }
    }
    
    public static void addJobs(JobDetail jobDetail, Trigger trigger){
    	Scheduler scheduler = schedulerFactoryBean.getScheduler();
    	try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}
