package com.oc.oneflow.executor.executorJob;

import com.oc.oneflow.Application;
import com.oc.oneflow.executor.job.HiveJob;
import com.oc.oneflow.executor.listener.OrderListener;
import com.oc.oneflow.model.scheduler.JobDescriptor;
import com.oc.oneflow.model.vo.StepVO;
import com.oc.oneflow.model.vo.TaskVO;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class ExecutorJob implements Job {
    private static final Logger appLogger = LoggerFactory.getLogger(Application.class);
    @Autowired
    private Scheduler scheduler;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap paramMap = jobExecutionContext.getMergedJobDataMap();
        String taskName = paramMap.getString("taskName");
        String taskId = paramMap.getString("taskId");
        String group = paramMap.getString("group");

        TaskVO taskVO = (TaskVO) paramMap.get("taskVO");

        OrderListener orderListener = new OrderListener(taskName + "OrderListener");

        appLogger.info("Get task " + taskId + "'s Config");

        taskVO.getSteps().sort(new Comparator<StepVO>() {
            @Override
            public int compare(StepVO o1, StepVO o2) {
                return Integer.parseInt(o1.getOrder()) - Integer.parseInt(o2.getOrder());
            }
        });

        Queue<JobDetail> jobDetailQueue = new LinkedList<>();
        Map<String, String> statusMap = new HashMap<>();
        taskVO.getSteps().forEach(stepVO -> {
            appLogger.info("Get step" + stepVO.getOrder() + "'s Config");
            statusMap.put(stepVO.getStepName(), "new");
            String type = stepVO.getType();
            JobDescriptor jobDescriptor = new JobDescriptor();
            jobDescriptor.setGroup(group);
            jobDescriptor.setName(taskName);
            jobDescriptor.setName(stepVO.getStepName());
            if (type.equals("hive")) {
                jobDescriptor.setJobClazz(HiveJob.class);
                paramMap.put("order", stepVO.getOrder());
                paramMap.put("stepName", stepVO.getStepName());
                paramMap.put("path", stepVO.getPath());
                paramMap.put("hiveParam", stepVO.getHiveParam());
            } else if (type.equals("spark")) {
//                    jobDescriptor.setJobClazz(SparkJob.class);
//                    paramMap.put("mainClass", stepVO.getPath());
//                    paramMap.put("jarPath", stepVO.getHiveParam());
            }
            jobDescriptor.setDataMap(paramMap);
            JobDetail jobDetail = jobDescriptor.buildJobDetail();
            jobDetailQueue.add(jobDetail);
        });
        List<String> runningJobList = new ArrayList<>();
        orderListener.setJobDetailQueue(jobDetailQueue);
        orderListener.setStatusMap(statusMap);
        orderListener.setRunningJobList(runningJobList);

        try {
            scheduler.getListenerManager().addJobListener(orderListener);
            if (jobDetailQueue.isEmpty()) {
                appLogger.warn(taskName + "'s Step List is empty");
                return;
            }
            while (!jobDetailQueue.isEmpty() && jobDetailQueue.peek().getJobDataMap().getString("order").equals("1")) {
                JobDetail initJobDetail = jobDetailQueue.poll();
                String stepName = initJobDetail.getJobDataMap().getString("StepName");
                runningJobList.add(stepName);
                statusMap.put(stepName, "processing");
                appLogger.info(stepName + " is running");
                scheduler.addJob(initJobDetail, true);
                scheduler.triggerJob(initJobDetail.getKey());
            }
        } catch (
                SchedulerException e) {
            appLogger.error("Error", e);
        }
    }
}
