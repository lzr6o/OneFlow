package com.oc.oneflow.executor.listener;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.listeners.JobChainingJobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderListener extends JobChainingJobListener {
    private static final Logger appLogger = LoggerFactory.getLogger(OrderListener.class);

    public OrderListener(String name) {
        super(name);
    }


    @Override
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        appLogger.info(jobDetail.getJobDataMap().getString("stepName") + " is done");
    }
}
