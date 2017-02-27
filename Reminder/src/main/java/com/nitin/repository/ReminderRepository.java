package com.nitin.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.nitin.model.Reminder;

@RepositoryRestResource
public interface ReminderRepository extends CrudRepository<Reminder, Long>{
	List<Reminder> findAll();
	List<Reminder> findByApplicationName(@Param ("appName") String applicationName);
}
