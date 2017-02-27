package com.nitin.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ReminderService {
    private static final Logger log = LoggerFactory.getLogger(ReminderService.class);

    public void writeDataToLog(String dataToWrite) {
        log.info("The data is : " + dataToWrite);
    }
}
