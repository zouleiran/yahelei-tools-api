package com.yahelei.domain.autotest;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class LabelBean implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String projectId;
    private int caseCount;
    private String create_time;
    private String description;
    private String update_time;
    private String host;
    private String paramlabel;
    private String caselist;
    public String getparamlabel() {
        return paramlabel;
    }
    public void setparamlabel(String paramlabel) {
        this.paramlabel = paramlabel;
    }
    public String gethost() {
        return host;
    }
    public void sethost(String host) {
        this.host = host;
    }

    public String getProjectId() {
        return projectId;
    }
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }
    private List<A> x;
    public List<A> getx() {
        return x;
    }
    public void setx(List<A> x) {
        this.x = x;
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
    public int getcaseCount() {
        return caseCount;
    }
    public void setcaseCount(int caseCount) {
        this.caseCount = caseCount;
    }
    public String getdescription() {
        return description;
    }
    public void setdescription(String description) {
        this.description = description;
    }
    public void setcaselist(String caselist) {
        this.caselist = caselist;
    }
    public String getcaselist() {
        return caselist;
    }
}
