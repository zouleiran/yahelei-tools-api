package com.yahelei.service.autotest;

import com.yahelei.domain.autotest.CaseBean;
import com.yahelei.domain.autotest.PreconditionBean;

import java.util.Map;

public interface CaseService {

    public String executecaseid(String caseid) throws Exception;
    public boolean executecaseid(String caseid,int taskid,long SerialId,
                                String host) throws Exception;
    public boolean executecaseid(String caseid,int taskid,String Labelid,long SerialId) throws Exception;
    public String execute(CaseBean caseBean
                          ) throws Exception;
    public void reduceLabel(int id);
    public String dopreProcessing(PreconditionBean p,String projectId);
    void addlabellist(String labellist,int caseid);

    boolean execute2(String caseid, int taskid, String labelid, long serialId, String host, Map parameter);
}