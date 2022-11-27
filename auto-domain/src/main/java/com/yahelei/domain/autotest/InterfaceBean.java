package com.yahelei.domain.autotest;

import java.util.Date;

public class InterfaceBean {
    private int id;
    private int num;
    private String method;
    private String url;
    private String fenzhi;
    private String projectdes;
    private String project_id;
    private String create_time;
    private String description;
    private String update_time;

    public String getprojectdes() {
        return projectdes;
    }
    public void setprojectdes(String projectdes) {
        this.projectdes = projectdes;
    }
    public int getid() {
        return id;
    }
    public void setid(int id) {
        this.id = id;
    }
    public int getnum() {
        return num;
    }
    public void setnum(int num) {
        this.num = num;
    }
    public String getcreate_time() {
        return create_time;
    }
    public void setcreate_time(String create_time) {
        this.create_time = create_time;
    }
    public String getupdate_time() {
        return update_time;
    }
    public void setupdate_time(String update_time) {
        this.update_time = update_time;
    }
    public String getdescription() {
        return description;
    }
    public void setdescription(String description) {
        this.description = description;
    }
    public String getfenzhi() {
        return fenzhi;
    }
    public void setfenzhi(String fenzhi) {
        this.fenzhi = fenzhi;
    }
    public String getproject_id() {
        return project_id;
    }
    public void setproject_id(String project_id) {
        this.project_id = project_id;
    }
    public String getmethod() {
        return method;
    }
    public void setmethod(String method) {
        this.method = method;
    }
    public String geturl() {
        return url;
    }
    public void seturl(String url) {
        this.url = url;
    }
}
