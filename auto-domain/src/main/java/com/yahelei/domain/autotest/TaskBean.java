package com.yahelei.domain.autotest;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class TaskBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String type;
    private String create_time;
    private String description;
    private String update_time;
    private String host;
    private String common_header;
    private String param;
    public String getparam() {
        return param;
    }
    public void setparam(String param) {
        this.param = param;
    }
    public String getcommon_header() {
        return common_header;
    }
    public void setcommon_header(String common_header) {
        this.common_header = common_header;
    }
    private String email;
    private String projectId;
    public String getprojectId() {
        return projectId;
    }
    public void setprojectId(String projectId) {
        this.projectId = projectId;
    }
    public String getemail() {
        return email;
    }
    public void setemail(String email) {
        this.email = email;
    }
    private String value;
    private String updateperson;
    public String getupdateperson() {
        return updateperson;
    }
    public void setupdateperson(String updateperson) {
        this.updateperson = updateperson;
    }
    private List<A> x;
    public List<A> getx() {
        return x;
    }
    public void setx(List<A> x) {
        this.x = x;
    }
    public String gethost() {
        return host;
    }
    public void sethost(String host) {
        this.host = host;
    }
    public String getvalue() {
        return value;
    }
    public void setvalue(String value) {
        this.value = value;
    }
    public int getid() {
        return id;
    }
    public void setid(int id) {
        this.id = id;
    }
    public String gettype() {
        return type;
    }
    public void settype(String type) {
        this.type = type;
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
}
