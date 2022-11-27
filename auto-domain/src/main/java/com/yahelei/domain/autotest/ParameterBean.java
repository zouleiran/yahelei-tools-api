package com.yahelei.domain.autotest;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ParameterBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String value;
    private Date create_time;
    private String description;
    private String update_time;
    public int getid() {
        return id;
    }
    public void setid(int id) {
        this.id = id;
    }
    public Date getcreate_time() {
        return create_time;
    }
    public void setcreate_time(Date create_time) {
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
    public String getname() {
        return name;
    }
    public void setname(String name) {
        this.name = name;
    }
    public String getvalue() {
        return value;
    }
    public void setvalue(String value) {
        this.value = value;
    }
}
