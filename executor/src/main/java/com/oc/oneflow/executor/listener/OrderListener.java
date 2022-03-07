package com.oc.oneflow.executor.listener;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OrderListener implements JobListener {
    private static final Logger appLogger = LoggerFactory.getLogger(OrderListener.class);

    @Override
    public String getName() {
        return null;
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext jobExecutionContext) {

    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext jobExecutionContext) {

    }

    @Override
    public void jobWasExecuted(JobExecutionContext jobExecutionContext, JobExecutionException e) {
        JobDetail jobDetail = jobExecutionContext.getJobDetail();
        appLogger.info(jobDetail.getJobDataMap().getString("stepName") + " is done");
    }
}
