package com.nitin.job;

import java.util.Date;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerListener;

public class SimpleTriggerListener implements TriggerListener{
	
	private Logger log = Logger.getLogger(SimpleTriggerListener.class);
	
	@Override
	public String getName() {
		return "SimpleTriggerListener";
	}

	@Override
	public void triggerFired(Trigger trigger, JobExecutionContext context) {
		log.info("triggerFired : "+trigger.getDescription());	
	}

	@Override
	public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
		if (trigger.getEndTime().compareTo(new Date()) < 0 ){
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void triggerMisfired(Trigger trigger) {
		log.info("triggerMisfired ");
	}

	@Override
	public void triggerComplete(Trigger trigger, JobExecutionContext context,
			CompletedExecutionInstruction triggerInstructionCode) {
		log.info("triggerComplete " + trigger.getDescription());	
	}

}
