package com.oc.oneflow.master;

import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class Application {
    @Value("${jsonConfigPath}")
    private String jsonConfigPath;

    private static final Logger appLogger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void run() {
        Gson gson = new Gson();
        ConfigVO configVO = gson.fromJson(jsonConfigPath, ConfigVO.class);
    }
}
