package com.yahelei.domain.autotest;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class ProjectBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String host;
    private String http;
    private String create_time;
    private String description;
    private String update_time;
    private String common_body;
    private String common_header;
    public String getcommon_body() {
        return common_body;
    }
    public void setcommon_body(String common_body) {
        this.common_body = common_body;
    }
    public String getcommon_header() {
        return common_header;
    }
    public void setcommon_header(String common_header) {
        this.common_header = common_header;
    }
    public int getid() {
        return id;
    }
    public void setid(int id) {
        this.id = id;
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
    public String gethost() {
        return host;
    }
    public void sethost(String host) {
        this.host = host;
    }
    public String gethttp() {
        return http;
    }
    public void sethttp(String http) {
        this.http = http;
    }
    public String getdescription() {
        return description;
    }
    public void setdescription(String description) {
        this.description = description;
    }
}
