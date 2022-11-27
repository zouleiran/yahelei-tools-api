package com.yahelei.domain.autotest;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class PreconditionBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String type;
    private String value;
    private String value1;
    private String value2;
    private String value3;
    private String action;
    private String Caselist;
    private String create_time;
    private String description;
    private String update_time;
    public String getaction() {
        return action;
    }
    public void setaction(String action) {
        this.action = action;
    }
    public String getvalue1() {
        return value1;
    }
    public void setvalue1(String value1) {
        this.value1 = value1;
    }
    public int getid() {
        return id;
    }
    public void setid(int id) {
        this.id = id;
    }
    public String getCaselist() {
        return Caselist;
    }
    public void setCaselist(String Caselist) {
        this.Caselist = Caselist;
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
    public String gettype() {
        return type;
    }
    public void settype(String type) {
        this.type = type;
    }
    public String getvalue() {
        return value;
    }
    public void setvalue(String value) {
        this.value = value;
    }
    public String getvalue2() {
        return value2;
    }
    public void setvalue2(String value2) {
        this.value2 = value2;
    }
    public String getvalue3() {
        return value3;
    }
    public void setvalue3(String value3) {
        this.value3 = value3;
    }
}
