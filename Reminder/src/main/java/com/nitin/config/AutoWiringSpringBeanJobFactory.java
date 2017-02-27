package com.nitin.config;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;

/**
 * In order for the jobs to be injected as beans, AutowiringSpringBeanJobFactory class which extends SpringBeanJobFactory and implements the ApplicationContextAware interface :
 * Adds autowiring support to quartz jobs.
 * Created by david on 2015-01-20.
 * @see https://gist.github.com/jelies/5085593
 */

public class AutoWiringSpringBeanJobFactory extends SpringBeanJobFactory implements
	ApplicationContextAware {
    private transient AutowireCapableBeanFactory beanFactory;

    @Override
    public void setApplicationContext(final ApplicationContext context) {
        beanFactory = context.getAutowireCapableBeanFactory();
    }

    @Override
    protected Object createJobInstance(final TriggerFiredBundle bundle) throws Exception {
        final Object job = super.createJobInstance(bundle);
        beanFactory.autowireBean(job);
        return job;
    }
}
