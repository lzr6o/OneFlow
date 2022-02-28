package com.oc.oneflow.model;

public class ConfigVO {
    private String projectId;
    private String projectName;
    private DataSource dataSource;
    private TaskVO tasks;

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public TaskVO getTaskVO() {
        return tasks;
    }

    public void setTaskVO(TaskVO taskVO) {
        this.tasks = taskVO;
    }

    class DataSource {
        private String url;
        private String userName;
        private String password;
        private String driver;
        private String name;
    }
}
