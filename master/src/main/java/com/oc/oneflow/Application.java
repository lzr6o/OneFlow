package com.oc.oneflow;

import com.oc.oneflow.common.utils.ConfigUtil;
import com.oc.oneflow.model.ConfigVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;

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
        System.out.println(configVO.getProjectId());
        System.out.println(configVO.getProjectName());
    }
}
