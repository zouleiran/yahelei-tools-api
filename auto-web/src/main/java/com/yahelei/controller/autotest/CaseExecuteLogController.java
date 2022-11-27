package com.yahelei.controller.autotest;

import com.yahelei.dao.mysql.autotest.CaseExecuteLogMapper;
import com.yahelei.dao.mysql.autotest.CaseMapper;
import com.yahelei.domain.autotest.CaseExecuteLogBean;
import com.yahelei.resentity.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
/**
 * Created by admin on 2018/7/23.
 */
@RestController
@CrossOrigin
@RequestMapping("/yahelei/caseExecuteLog")
public class CaseExecuteLogController{
    @Resource
    public CaseExecuteLogMapper caseExecuteLogMapper;
    private static final Logger logger = LoggerFactory.getLogger(CaseExecuteLogController.class);
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<?> list(@RequestParam(value="caseid",required =false,defaultValue = "-1") int caseid,
                          @RequestParam(value="projectid",required =false,defaultValue = "") String projectid,
                          @RequestParam(value="interfaceid",required =false,defaultValue = "") String interfaceid
    )
    {
        logger.info("/caseExecuteLog/list GET 方法被调用!!");
        CaseExecuteLogBean c=new CaseExecuteLogBean();
        c.setCaseId(caseid);
        c.setProjectId(projectid);
        c.setInterfaceId(interfaceid);
        List<CaseExecuteLogBean> list =caseExecuteLogMapper.getlistbycaseid(c);
        Result<List<CaseExecuteLogBean>> result = new Result<>();

        result.setResult(list);
        result.setSuccess(true);
        return result;
    }
    @RequestMapping(value = "/getlist", method = RequestMethod.GET)
    public Result<?> getlist(@RequestParam(value="serialId",required =false) String serialId2,
                             @RequestParam(value="taskid",required =false) String taskid2,
                             @RequestParam(value="pagecount",required =false,defaultValue = "10") Integer pagecount,
                             @RequestParam(value="page",required =false,defaultValue = "1") Integer page)
    {
        logger.info("/caseExecuteLog/getlist GET 方法被调用!!");
        CaseExecuteLogBean c=new CaseExecuteLogBean();
        long serialId= (serialId2==null||serialId2.length()==0)?-1:Long.parseLong(serialId2);
        int taskid= (taskid2==null||taskid2.length()==0)?-2:Integer.parseInt(taskid2);
        c.setSerialId(serialId);
        c.setTaskId(taskid);
        List<CaseExecuteLogBean> list =caseExecuteLogMapper.getlist(serialId,taskid,pagecount,(page-1)*pagecount);
        for(CaseExecuteLogBean caseExecuteLogBean:list){
            caseExecuteLogBean.setrate((float)caseExecuteLogBean.getsuccess()/(float)caseExecuteLogBean.getsum());
        }
        int sum=caseExecuteLogMapper.getsum(serialId,taskid).size();
        if(list.size()>0)
            list.get(0).setId(sum);
        Result<List<CaseExecuteLogBean>> result = new Result<>();
        result.setMessage(sum+"");
        result.setResult(list);
        result.setSuccess(true);
        return result;
    }
    @RequestMapping(value = "/getdetail", method = RequestMethod.GET)
    public Result<?> getdetail(@RequestParam(value="serialId",required =false) String serialId2)
    {
        logger.info("/caseExecuteLog/getdetail GET 方法被调用!!");

        long serialId= (serialId2==null||serialId2.length()==0)?-1:Long.parseLong(serialId2);
        List<CaseExecuteLogBean> a =caseExecuteLogMapper.getdetail(serialId);
        a.sort(new Comparator<CaseExecuteLogBean>() {
            @Override
            public int compare(CaseExecuteLogBean o1, CaseExecuteLogBean o2) {
                if(o1.getresult()!=o2.getresult())
                {
                    if(o1.getresult())
                        return 1;
                    else
                        return -1;
                }
                else
                    return 0;
            }
        });
        Result<List<CaseExecuteLogBean>> result = new Result<>();

        result.setResult(a);
        result.setSuccess(true);
        return result;
    }
}
