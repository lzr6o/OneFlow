package com.oc.oneflow.executor.config;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.util.Assert;

@Configuration
public class SchedulerConfiguration {
    @Bean
    public SchedulerFactoryBean schedulerFactory(ApplicationContext applicationContext) {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setJobFactory(new AutowireCapableBeanJobFactory(applicationContext));
        return schedulerFactoryBean;
    }

    @Bean
    public Scheduler scheduler(ApplicationContext applicationContext) throws SchedulerException {
        Scheduler scheduler = schedulerFactory(applicationContext).getScheduler();
        scheduler.start();
        return scheduler;
    }

    public class AutowireCapableBeanJobFactory extends SpringBeanJobFactory {
        private final AutowireCapableBeanJobFactory beanFactory;

        @Autowired
        public AutowireCapableBeanJobFactory(AutowireCapableBeanJobFactory beanJobFactory) {
            Assert.notNull(beanJobFactory, "Bean factory must not be null");
            this.beanFactory = beanJobFactory;
        }

        @Override
        protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
            Object jobInstance = super.createJobInstance(bundle);
            this.beanFactory.autowireBean(jobInstance);
            this.beanFactory.initializeBean(jobInstance, "jobInstance");
            return jobInstance;
        }
    }
}
