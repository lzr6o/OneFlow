package com.oc.oneflow.executor.job;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataloaderJob implements Job {
    private static final Logger appLogger = LoggerFactory.getLogger(DataloaderJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        appLogger.info("Begin to run Dataloader job");
        JobDataMap paramMap = jobExecutionContext.getMergedJobDataMap();
        Map<String, Object> dataSourceMap = (Map<String, Object>) paramMap.get("datasourceMap");
        try {
            JdbcTemplate sourceJtm = (JdbcTemplate) dataSourceMap.get(paramMap.getString("sourceDataSource"));
            JdbcTemplate destJtm = (JdbcTemplate) dataSourceMap.get(paramMap.getString("destDataSource"));
            List<String> sourceTables = (List<String>) paramMap.get("sourceTables");
            List<String> destTables = (List<String>) paramMap.get("destTables");
            if (sourceTables == null || destTables == null || sourceTables.isEmpty() || destTables.isEmpty() || sourceTables.size() != destTables.size()) {
                throw new Exception("source table list and dest table list must have value and same size");
            }
            for (int i = 0; i < sourceTables.size(); i++) {
                String sourceTable = sourceTables.get(i);
                String destTable = destTables.get(i);
                appLogger.info("Being to load data from " + sourceTable);
                /*
                 * id name age
                 * 1  John  10
                 * 2  Paul  5
                 * */
                appLogger.info("select * from " + sourceTable);
                List<Map<String, Object>> res = sourceJtm.queryForList("select * from " + sourceTable);
                if (res.isEmpty()) {
                    appLogger.info("No data found in table " + sourceTable);
                } else {
                    Set<String> keySet = res.get(0).keySet();
                    List<String> keyList = new ArrayList(keySet);
                    List<String> valueStrList = new ArrayList<>();
                    res.forEach(rowMap -> {
                        List<String> valueList = new ArrayList<>();
                        keyList.forEach(key -> {
                            valueList.add(String.valueOf(rowMap.get(key)));
                        });
                        String valueStr = "(" + String.join(",", valueList) + ")";
                        valueStrList.add(valueStr);
                    });
                    appLogger.info("insert into " + destTable + "(" + String.join(",", keyList) + ") values(" + String.join(",", valueStrList) + ")");
                    destJtm.execute("insert into " + destTable + "(" + String.join(",", keyList) + ") values(" + String.join(",", valueStrList) + ")");
                }
                appLogger.info("Load data from " + sourceTable + " to " + destTable + " complete");
            }
            appLogger.info("Dataloader job is done");
        } catch (Exception e) {
            appLogger.error("Error: ", e);
        }
    }
}
