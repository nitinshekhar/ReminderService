package com.nitin.controller;

import java.util.List;

import javax.annotation.PostConstruct;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
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

    private SchedulerFactoryBean schedulerFactoryBean;
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
                scheduler.scheduleJob(model.getJobDetail(), model.getTrigger());
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
}
