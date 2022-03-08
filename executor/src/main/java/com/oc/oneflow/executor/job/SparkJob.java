package com.oc.oneflow.executor.job;

import com.oc.oneflow.Application;
import org.apache.spark.launcher.SparkLauncher;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class SparkJob implements Job {
    private static final Logger appLogger = LoggerFactory.getLogger(Application.class);

    @Value("${sparkHome}")
    private String sparkHome;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap map = context.getMergedJobDataMap();
        appLogger.info("Begin to run " + map.getString("taskName") + " " + map.getString("stepName"));
        try {
            SparkLauncher sparkLauncher = new SparkLauncher()
                    .setAppResource(map.getString("path"))
                    .setMainClass(map.getString("className"))
                    .setMaster(map.getString("master"))
                    .setSparkHome(sparkHome)
                    .setDeployMode(map.getString("deployMode"))
//                    .addFile()
//                    .addAppArgs("123", "456")
//                    .addSparkArg("--queue", map.getString("queue"))
                    ;
            List<String> confList = null;
            if (map.get("conf") != null) {
                confList = (List<String>) map.get("conf");
            }
            List<String> sparkArgList = null;
            if (map.get("sparkArg") != null) {
                sparkArgList = (List<String>) map.get("sparkArg");
            }
            List<String> appArgList = null;
            if (map.get("appArgs") != null) {
                appArgList = (List<String>) map.get("appArgs");
            }
            if (map.getString("propertiesFile") != null) {
                sparkLauncher.setPropertiesFile(map.getString("propertiesFile"));
            }
            if (map.get("files") != null) {
                List<String> fileList = (List<String>) map.get("files");
                for (String file : fileList) {
                    sparkLauncher.addFile(file);
                }
            }
            if (confList != null) {
                for (String conf : confList) {
                    String confName = conf.substring(0, conf.indexOf("="));
                    String confValue = conf.substring(conf.indexOf("=") + 1);
                    sparkLauncher.setConf(confName, confValue);
                }
            }
            if (appArgList != null) {
                String[] appArgs = appArgList.stream().toArray(String[]::new);
                sparkLauncher.addAppArgs(appArgs);
            }
            if (sparkArgList != null) {
                for (String sparkArg : sparkArgList) {
                    String sparkArgName = sparkArg.substring(0, sparkArg.indexOf('='));
                    String sparkArgValue = sparkArg.substring(sparkArg.indexOf('=') + 1);
                    if (sparkArgName.startsWith("--")) {
                        sparkLauncher.addSparkArg(sparkArgName, sparkArgValue);
                    } else {
                        sparkLauncher.addSparkArg("--" + sparkArgName, sparkArgValue);
                    }
                }
            }
            appLogger.info("Task start, run main class: " + map.getString("className"));

            appLogger.info("Task is done");
        } catch (Exception e) {
            appLogger.error("Execute Script Error: ", e);
        } finally {
            appLogger.info("Finish run " + map.getString("taskName") + " " + map.getString("stepName"));
        }
    }
}
