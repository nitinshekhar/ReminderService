package com.nitin;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ImportResource;

import com.nitin.job.ReminderJobScheduleProperties;

@SpringBootApplication
@ImportResource({"classpath:bean-context.xml"})	
@EnableConfigurationProperties(value = {ReminderJobScheduleProperties.class})

public class ReminderApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReminderApplication.class, args);
	}
}
