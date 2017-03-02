package com.nitin.service;

import java.util.List;

import com.nitin.model.Reminder;

public interface ReminderService {

	public List<Reminder> findAll();
	public List<Reminder> findByApplicationName(String applicationName);
	
}
