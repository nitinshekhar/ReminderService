package com.nitin.job;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Maps to the root of the configuration and has a property of a List of
 * JobProperties objects.
 */
// The configuration Property loads from the application.yml file and accordingly sets the jobs.

@ConfigurationProperties(prefix = "schedule")
public class ReminderJobScheduleProperties {
    @NotNull
    @NotEmpty
    @Valid
    private List<ReminderJobProperties> jobs;
 
    public List<ReminderJobProperties> getJobs() {
       return jobs;
    }
 
    public void setJobs(List<ReminderJobProperties> jobs) {
       this.jobs = jobs;
    }
}
