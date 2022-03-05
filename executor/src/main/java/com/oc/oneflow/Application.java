package com.oc.oneflow;

import com.oc.oneflow.common.utils.ConfigUtil;
import com.oc.oneflow.executor.service.HiveService;
import com.oc.oneflow.model.ConfigVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.util.List;

@SpringBootApplication
public class Application {
    private static final Logger appLogger = LoggerFactory.getLogger(Application.class);
    @Autowired
    private ConfigUtil configUtil;
    @Autowired
    private HiveService hiveService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void run() throws FileNotFoundException {
        appLogger.info("OneFlow begin to run");
        ConfigVO configVO = configUtil.getConfigVO();
        appLogger.info("Get ConfigVO");
        List<ConfigVO.DataSource> dataSources = configVO.getDataSources();
        appLogger.info("Get Data Source Config");
        configVO.getTasks().forEach(taskVO -> {
            String taskId = taskVO.getTaskId();
            String taskName = taskVO.getTaskName();
            String cron = taskVO.getCron();
            appLogger.info("Get task " + taskId + "'s Config");
            taskVO.getSteps().forEach(stepVO -> {
                appLogger.info("Get step" + stepVO.getOrder() + "'s Config");
                String type = stepVO.getType();
                if (type.equals("hive")) {
                    appLogger.info("Run hive");
                    hiveService.runHql(stepVO.getPath(), stepVO.getHiveParam());
                }
            });
        });
    }
}
