package com.yahelei.service.autotest.impl;

import com.yahelei.dao.mysql.autotest.CaseMapper;
import com.yahelei.dao.mysql.autotest.LabelMapper;
import com.yahelei.domain.autotest.A;
import com.yahelei.domain.autotest.CaseBean;
import com.yahelei.domain.autotest.LabelBean;
import com.yahelei.service.autotest.CaseService;
import com.yahelei.service.autotest.LabelService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LabelImpl implements LabelService {
    @Resource
    public LabelMapper labelMapper;
    @Resource
    public CaseMapper caseMapper;
    @Override
    public List<LabelBean> getLabelList(String project_id, String description){
        List<LabelBean> a = labelMapper.getLabelList(project_id,description);
        for(LabelBean l:a)
        {
            List<CaseBean> caselist = caseMapper.getcaselistbyInterface_id("", l.getProjectId());
            List<A> a2=new ArrayList();
            for(CaseBean c2:caselist){
                A a3=new A();
                a3.setdescription(c2.getdescription());
                a3.setid(c2.getid());
                a2.add(a3);
            }
            l.setx(a2);
        }
        return a;
    }
    @Override
    public void updatelabel(LabelBean labelBean){//需要先把case表里面含有该lable的case删去该label，在加上去label
        String caselist=labelMapper.getdetailbyid(labelBean.getid()+"").getcaselist();
        if(caselist.length()>0) {
            String[] caselistold =caselist.split(",");
            for (String caseold : caselistold) {
                CaseBean casebean = caseMapper.getdetailbyid(caseold);
                String[] label = casebean.getlabellist().split(",");
                StringBuffer sb = new StringBuffer();
                for (String label2 : label) {
                    if (!label2.equals(labelBean.getid() + ""))
                        sb.append(label2 + ",");
                }
                if(sb.length()>0)
                    casebean.setlabellist(sb.toString().substring(0, sb.length() - 1));
                else
                    casebean.setlabellist("");
                caseMapper.updatecase(casebean);
            }
        }
        labelMapper.updatelabel(labelBean);
        String caselist2=labelBean.getcaselist();
        if(caselist2.length()>0) {
            String[] caselistnew = caselist2.split(",");
            for (String casenew : caselistnew) {
                CaseBean casebean = caseMapper.getdetailbyid(casenew);
                String label = casebean.getlabellist();
                if(label.length()==0)
                    label=labelBean.getid()+"";
                else
                    label = label + "," + labelBean.getid();
                casebean.setlabellist(label);
                caseMapper.updatecase(casebean);
            }
        }
    }

    @Override
    public void insertlabel(LabelBean labelBean){

        labelMapper.insertlabel(labelBean);
        String caselist2=labelBean.getcaselist();
        if(caselist2.length()>0) {
            String[] caselistnew = caselist2.split(",");
            for (String casenew : caselistnew) {
                CaseBean casebean = caseMapper.getdetailbyid(casenew);
                String label = casebean.getlabellist();
                label = label + "," + labelBean.getid();
                casebean.setlabellist(label);
                caseMapper.updatecase(casebean);
            }
        }
    }
    @Resource
    CaseService caseService;
    @Override
    public String executebylabelid(String Labelid){
        //先把host换了
        //再把parameter换了
        //不换了不能影响别的执行，将以变量传递
        //再把caselist拿到
        LabelBean x=labelMapper.getdetailbyid(Labelid);
        String c[]=x.getcaselist().split(",");
        //for循环执行
        long SerialId=System.currentTimeMillis()/1000;
        boolean flag=true;
        for(String caseid:c)
        {
            try {
                if(!caseService.executecaseid(caseid,-1,Labelid,SerialId))
                    flag=false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return flag==true?"true":"false";
    }
}