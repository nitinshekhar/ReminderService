package com.nitin.job;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nitin.service.ReminderServiceImpl;


@Component
@DisallowConcurrentExecution
public class ReminderJob implements Job {

    @Autowired
    private ReminderServiceImpl reminderService;
    
	private String dataToWrite;

	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		reminderService.writeDataToLog(dataToWrite);
	}

    public void setDataToWrite(String dataToWrite) {
        this.dataToWrite = dataToWrite;
    }

}
