package com.nitin;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource({"classpath:bean-context.xml"})

public class ReminderApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReminderApplication.class, args);
		// @TODO Consuming WebSerice as part of Reminder
		// @TODO Changing the RestService to take an object rather than primitive parameters
		// @TODO Security for RestService in Spring boot
		// @TODO Test Case for Spring Boot App
		// @TODO Group the job based on calling app etc.
		// @TODO Deregister the Job based on request
		// @TODO List the job properly on the browser
		// @TODO Spring mvc
	}
}
