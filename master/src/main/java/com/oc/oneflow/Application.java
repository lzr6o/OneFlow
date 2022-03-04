package com.oc.oneflow;

import com.oc.oneflow.common.utils.ConfigUtil;
import com.oc.oneflow.model.ConfigVO;
import com.oc.oneflow.model.StepVO;
import com.oc.oneflow.model.TaskVO;
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

    @Autowired
    private ConfigUtil configUtil;

    private static final Logger appLogger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void run() throws FileNotFoundException {
        ConfigVO configVO = configUtil.getConfigVO();
        List<TaskVO> taskVOList = configVO.getTasks();
        for (TaskVO taskVO : taskVOList) {
            List<StepVO> stepVOList = taskVO.getSteps();
            for (StepVO stepVO : stepVOList) {
                stepVO.getType();
            }
        }
    }
}
