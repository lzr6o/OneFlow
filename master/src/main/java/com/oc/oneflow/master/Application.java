package com.oc.oneflow.master;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.oc.oneflow.model.ConfigVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.io.FileNotFoundException;
import java.io.FileReader;

@SpringBootApplication
public class Application {
    @Value("${jsonConfigPath}")
    private String jsonConfigPath;

    private static final Logger appLogger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @PostConstruct
    public void run() throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(jsonConfigPath));
        ConfigVO configVO = gson.fromJson(reader, ConfigVO.class);
        System.out.println(configVO.getProjectId());
        System.out.println(configVO.getProjectName());
    }
}
