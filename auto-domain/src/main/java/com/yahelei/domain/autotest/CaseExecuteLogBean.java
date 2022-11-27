package com.yahelei.domain.autotest;

import java.util.Date;

public class CaseExecuteLogBean {
    private Integer id;
    private Integer sum;
    private float rate;
    private Integer success;
    private String description;
    private String path;
    private String labelId;
    private String projectId;
    private String interfaceId;
    private String env;
    private long serialId;
    private Integer caseId;
    private Integer taskId;
    private String taskname;
    public String getlabelId() {
        return labelId;
    }
    public void setlabelId(String labelId) {
        this.labelId = labelId;
    }
    public String gettaskname() {
        return taskname;
    }
    public void settaskname(String taskname) {
        this.taskname = taskname;
    }
    private String url;
    private String responseResult;
    private String header;
    public String getheader() {
        return header;
    }
    public void setheader(String header) {
        this.header = header;
    }
    public String getprecondition() {
        return precondition;
    }
    public void setprecondition(String precondition) {
        this.precondition = precondition;
    }
    private String precondition;
    private String parameters;
    private String expectedResult;
    private String create_time;
    private String update_time;
    private long execTime;
    private boolean result;
    private String taskdescription;
    public String gettaskdescription() {
        return taskdescription;
    }
    public void settaskdescription(String taskdescription) {
        this.taskdescription = taskdescription;
    }
    private String casedescription;
    public String getcasedescription() {
        return casedescription;
    }
    public void setcasedescription(String casedescription) {
        this.casedescription = casedescription;
    }
    @Override
    public String toString() {
        return "CaseExecuteLogBean{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", interfaceId=" + interfaceId +
                ", serialId=" + serialId +
                ", caseId=" + caseId +
                ", taskId=" + taskId +
                ", url='" + url + '\'' +
                ", responseResult='" + responseResult + '\'' +
                ", parameters='" + parameters + '\'' +
                ", expectedResult='" + expectedResult + '\'' +
                ", execTime=" + execTime +
                ", result=" + result +
                ", path='" + path + '\'' +
                '}';
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public float getrate() {
        return rate;
    }

    public void setrate(float rate) {
        this.rate = rate;
    }
    public Integer getsum() {
        return sum;
    }

    public void setsum(Integer sum) {
        this.sum = sum;
    }
    public Integer getsuccess() {
        return success;
    }

    public void setsuccess(Integer success) {
        this.success = success;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }

    public String getInterfaceId() {
        return interfaceId;
    }

    public void setInterfaceId(String interfaceId) {
        this.interfaceId = interfaceId;
    }

    public String getenv() {
        return env;
    }

    public void setenv(String env) {
        this.env = env;
    }

    public long getSerialId() {
        return serialId;
    }

    public void setSerialId(long serialId) {
        this.serialId = serialId;
    }

    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getResponseResult() {
        return responseResult;
    }

    public void setResponseResult(String responseResult) {
        this.responseResult = responseResult;
    }

    public String getParameters() {
        return parameters;
    }

    public void setParameters(String parameters) {
        this.parameters = parameters;
    }

    public String getExpectedResult() {
        return expectedResult;
    }

    public void setExpectedResult(String expectedResult) {
        this.expectedResult = expectedResult;
    }

    public String getCreateTime() {
        return create_time;
    }

    public void setCreateTime(String create_time) {
        this.create_time = create_time;
    }

    public String getupdate_time() {
        return update_time;
    }

    public void setupdate_time(String update_time) {
        this.update_time = update_time;
    }
    public long getExecTime() {
        return execTime;
    }

    public void setExecTime(long execTime) {
        this.execTime = execTime;
    }

    public boolean getresult() {
        return result;
    }

    public void setresult(boolean result) {
        this.result = result;
    }

}
