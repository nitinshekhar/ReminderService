package com.nitin.job;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;

import java.util.ArrayList;
import java.util.List;

import org.quartz.CronExpression;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Component;

import com.nitin.model.Reminder;
import com.nitin.service.ReminderService;
import com.nitin.util.AppConstant;
import com.nitin.util.ApplicationUtil;

/**
 * Generates a list of JobScheduleModel based on the list of Job registeed in the table
 * @author nitinshekhar
 *
 */

@Component
public class ReminderJobSchedulerModelGenerator {

	private static Logger log = LoggerFactory.getLogger(ReminderJobSchedulerModelGenerator.class);
	private ReminderService reminderService;
	
    @Autowired
    public ReminderJobSchedulerModelGenerator(ReminderService reminderService) {
        this.reminderService = reminderService;
    }
    
    /*
     * Retrieves a list of job from the database and create a list of model to schedule
     */
    public List<ReminderJobScheduleModel> generateModels() {
    	// Retrieve the list from table
    	List<Reminder> jobs = reminderService.findAll();
    	
    	//Verify in case if the list is Empty
        if (jobs.isEmpty()){
        	log.info("No Jobs in the table to Schedule");
        }
        
        List<ReminderJobScheduleModel> generatedModels = new ArrayList<>();
        for (int i = 0; i < jobs.size(); i++) {
            ReminderJobScheduleModel model = generateModelFrom(jobs.get(i), i);
            generatedModels.add(model);
        }
        return generatedModels;
    }
    
    /*
     * Created Job Detail and Trigger for individual job and adds into the Model
     */
    public ReminderJobScheduleModel generateModelFrom(Reminder job, int jobIndex) {
    	ReminderJobScheduleModel jobScheduleModel=null;
        JobDetail jobDetail = getJobDetailFor(AppConstant.JOB_NAME + jobIndex, AppConstant.GROUP_NAME, job);
        if (ApplicationUtil.compareDate(job.getStartDateTime(),job.getEndDateTime()) < 0){
        	CronTriggerFactoryBean trigger = createCronTrigger(jobDetail, job);
            jobScheduleModel = new ReminderJobScheduleModel(jobDetail, trigger.getObject());
        } else {
        	SimpleTriggerFactoryBean trigger = createSimpleTrigger(jobDetail, job);
            jobScheduleModel = new ReminderJobScheduleModel(jobDetail, trigger.getObject());
       }

        return jobScheduleModel;
    }
    
    /*
     * Creates the Job Detail Object for given job
     */
    private static JobDetail getJobDetailFor(String jobName, String groupName, Reminder job) {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(ReminderJob.class);
        jobDetailFactory.setJobDataMap(getJobDataMapFrom(job.getReturnURL()));
        jobDetailFactory.setDescription("Job with data to write : " + job.getReturnURL() + " and CRON expression : " + job.getCronExpression());
        jobDetailFactory.setGroup(groupName); 
        jobDetailFactory.setName(jobName);
        //Durability needs to be true to store in database
        jobDetailFactory.setDurability(true);
        JobDetail jobDetail = (JobDetail)jobDetailFactory.getObject();
        return jobDetail;
    }
    
    /*
     * Add Job Data Map to save the data that can be accessed by the Job implementation
     */
    private static JobDataMap getJobDataMapFrom(String dataToWrite) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put(AppConstant.DATA_TO_WRITE, dataToWrite);
        return jobDataMap;
    }
    
    /*
     * Creates a Cron Trigger for reoccurance job
     */
    public static CronTriggerFactoryBean createCronTrigger(JobDetail jobDetail, Reminder job){
    	CronTriggerFactoryBean trigger = new CronTriggerFactoryBean();
       	trigger.setJobDetail(jobDetail);
    	trigger.setStartTime(job.getStartDateTime());
    	trigger.setCronExpression(job.getCronExpression());
    	trigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
    	trigger.setDescription("Cron Schedule");
    	return trigger;
    }
    
    /*
     * Creates Simple Trigger for one time job
     */
    public static SimpleTriggerFactoryBean createSimpleTrigger(JobDetail jobDetail, Reminder job){
    	SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
    	trigger.setJobDetail(jobDetail);
    	trigger.setStartTime(job.getStartDateTime());
    	trigger.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
    	trigger.setDescription("Single Schedule");
    	trigger.afterPropertiesSet();
    	return trigger;
    }
    
    /*
     * Used for adding Trigger based on the request
     */
    public static Trigger getTriggerFor(Reminder job, JobDetail jobDetail) {

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
    			log.info("Invalid Cron Expression");
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
       return trigger;
    }
}
