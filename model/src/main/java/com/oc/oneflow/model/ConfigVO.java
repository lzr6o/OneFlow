package com.oc.oneflow.model;

import java.util.List;

public class ConfigVO {
    private Integer projectId;
    private String projectName;
    private List<DataSource> dataSource;
    private List<TaskVO> tasks;

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public List<DataSource> getDataSource() {
        return dataSource;
    }

    public void setDataSource(List<DataSource> dataSource) {
        this.dataSource = dataSource;
    }

    public List<TaskVO> getTasks() {
        return tasks;
    }

    public void setTasks(List<TaskVO> tasks) {
        this.tasks = tasks;
    }

    class DataSource {
        private String url;
        private String userName;
        private String password;
        private String driver;
        private String name;
    }
}
