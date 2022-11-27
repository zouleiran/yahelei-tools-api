package com.yahelei.domain.autotest;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class AddressBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String updateperson;
    public String getupdateperson() {
        return updateperson;
    }
    public void setupdateperson(String updateperson) {
        this.updateperson = updateperson;
    }
    private String type;
    private String url;
    private String port;
    private String username;
    private String password;
    private String value;
    private String create_time;
    private String description;
    private String update_time;
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
    public String geturl() {
        return url;
    }
    public void seturl(String url) {
        this.url = url;
    }
    public String getport() {
        return port;
    }
    public void setport(String port) {
        this.port = port;
    }
    public String getusername() {
        return username;
    }
    public void setusername(String username) {
        this.username = username;
    }
    public String getpassword() {
        return password;
    }
    public void setpassword(String password) {
        this.password = password;
    }
    public String getvalue() {
        return value;
    }
    public void setvalue(String value) {
        this.value = value;
    }

    public String getdescription() {
        return description;
    }
    public void setdescription(String description) {
        this.description = description;
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
}
