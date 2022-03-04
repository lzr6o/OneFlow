package com.oc.oneflow.executor.service.impl;

import com.oc.oneflow.Application;
import com.oc.oneflow.executor.service.HiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class HiveServiceImpl implements HiveService {
    private static final Logger appLogger = LoggerFactory.getLogger(Application.class);
    @Autowired
    private JdbcTemplate hiveJdbcTemplate;

    @Override
    public void execute(String sql) {
        appLogger.info(sql);
        hiveJdbcTemplate.execute(sql);
        appLogger.info("Hive sql executed");
    }
}
