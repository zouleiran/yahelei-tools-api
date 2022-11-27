package com.yahelei.domain.autotest;

import java.util.Date;
import java.util.List;

public class CaseBean {
    private int id;
    private int num;
    private String method;
    private String expect_sql;
    private String Interface_id;
    private String http;
    private String host;
    private String httpend;
    private String env;
    private String postcondition;
    private String expect;
    private boolean Result;
    private String ResponseResult;
    private String body_type2;
    private String sqlid;
    private String Resultcode;
    private String body_type;
    private String body;
    private String body2;
    private String sign;
    private String signtype;
    private String header;
    private String labellist;
    private List<LabelBean> x;
    private String precondition;
    private String url;
    private String project_id;
    private String create_time;
    private String description;
    private long ExecTime;
    private String update_time;
    public String getbody_type2() {
        return body_type2;
    }
    public void setbody_type2(String body_type2) {
        this.body_type2 = body_type2;
    }
    public String getsign() {
        return sign;
    }
    public void setsign(String sign) {
        this.sign = sign;
    }
    public String getsigntype() {
        return signtype;
    }
    public void setsigntype(String signtype) {
        this.signtype = signtype;
    }
    public String getlabellist() {
        return labellist;
    }
    public void setlabellist(String labellist) {
        this.labellist = labellist;
    }
    public List<LabelBean> getx() {
        return x;
    }
    public void setx(List<LabelBean> x) {
        this.x = x;
    }
    public String gethttpend() {
        return httpend;
    }
    public void sethttpend(String httpend) {
        this.httpend = httpend;
    }
    public String gethttp() {
        return http;
    }
    public void sethttp(String http) {
        this.http = http;
    }
    public String gethost() {
        return host;
    }
    public void sethost(String host) {
        this.host = host;
    }
    public String getsqlid() {
        return sqlid;
    }
    public void setsqlid(String sqlid) {
        this.sqlid = sqlid;
    }
    public String getenv() {
        return env;
    }
    public void setenv(String env) {
        this.env = env;
    }
    public boolean getResult() {
        return Result;
    }
    public void setResult(boolean Result) {
        this.Result = Result;
    }
    public long getExecTime() {
        return ExecTime;
    }
    public void setExecTime(Long ExecTime) {
        this.ExecTime = ExecTime;
    }
    public String getResultcode() {
        return Resultcode;
    }
    public void setResultcode(String Resultcode) {
        this.Resultcode = Resultcode;
    }
    public String getInterface_id() {
        return Interface_id;
    }
    public void setInterface_id(String Interface_id) {
        this.Interface_id = Interface_id;
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
    public String getbody2() {
        return body2;
    }
    public void setbody2(String body2) {
        this.body2 = body2;
    }
    public String geturl() {
        return url;
    }
    public void seturl(String url) {
        this.url = url;
    }
    public String getprecondition() {
        return precondition;
    }
    public void setprecondition(String precondition) {
        this.precondition = precondition;
    }
    public String getheader() {
        return header;
    }
    public void setheader(String header) {
        this.header = header;
    }
    public String getpostcondition() {
        return postcondition;
    }
    public void setpostcondition(String postcondition) {
        this.postcondition = postcondition;
    }
    public String getbody_type() {
        return body_type;
    }
    public void setbody_type(String body_type) {
        this.body_type = body_type;
    }
    public String getexpect_sql() {
        return expect_sql;
    }
    public void setexpect_sql(String expect_sql) {
        this.expect_sql = expect_sql;
    }
    public String getexpect() {
        return expect;
    }
    public void setexpect(String expect) {
        this.expect = expect;
    }
    public String getbody() {
        return body;
    }
    public void setbody(String body) {
        this.body = body;
    }
    public void setResponseResult(String ResponseResult) {
        this.ResponseResult=ResponseResult;
    }
    public String getResponseResult() {
        return ResponseResult;
    }
}
