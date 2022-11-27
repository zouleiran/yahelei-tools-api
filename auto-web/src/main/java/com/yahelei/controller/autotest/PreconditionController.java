package com.yahelei.controller.autotest;

import com.yahelei.dao.mysql.autotest.CaseMapper;
import com.yahelei.dao.mysql.autotest.PreconditionMapper;
import com.yahelei.domain.autotest.PreconditionBean;
import com.yahelei.resentity.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by admin on 2018/7/23.
 */
@RestController
@CrossOrigin
@RequestMapping("/yahelei/precondition")
public class PreconditionController{
    @Resource
    public PreconditionMapper preconditionMapper;
    @Resource
    public CaseMapper caseMapper;
    private static final Logger logger = LoggerFactory.getLogger(PreconditionController.class);
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<?> list(@RequestParam(value="type",required =false) String type,
                          @RequestParam(value="description",required =false) String description)
    {
        logger.info("/precondition/list GET 方法被调用!!");
        List<PreconditionBean> list =preconditionMapper.getPreconditionList(type,description);
//        for(PreconditionBean p:list){
//            String Precondition= "%,"+p.getid()+",%";
//            String Precondition1= "%,"+p.getid()+"]";
//            String Precondition2= "["+p.getid()+",%";
//            String Precondition3= "["+p.getid()+"]";
//            String Precondition4= "%(1)"+p.getid()+"\"%";
//            p.setCaselist(caseMapper.getpreconditionnum(Precondition1,Precondition,Precondition2,Precondition3,Precondition4).toString());
//        }
        Result<List<PreconditionBean>> result = new Result<>();
        result.setResult(list);
        result.setSuccess(true);
        return result;
    }
    @RequestMapping(value = "/deleteprecondition", method = RequestMethod.GET)
    @ResponseBody
    public Result<?> deleteprecondition(@RequestParam(value="PreconditionId", defaultValue = "-1") Integer PreconditionId) {
        logger.info("/precondition/deleteparameter GET 方法被调用!!");
        preconditionMapper.deletePrecondition(PreconditionId);
        Result<String> result = new Result<>();
        result.setResult("删除成功");
        result.setSuccess(true);
        return result;
    }
    @RequestMapping(value = "/addoretidprecondition", method = RequestMethod.POST)
    @ResponseBody
    public Result<?> addoretidparameter(
            @RequestParam(value="preId", defaultValue = "0") Integer  preId,
            @RequestParam(value="type") String type,
            @RequestParam(value="value",required =false, defaultValue = "") String value,
            @RequestParam(value="value1",required =false, defaultValue = "") String value1,
            @RequestParam(value="value2" ,required =false, defaultValue = "") String value2,
            @RequestParam(value="value3" ,required =false, defaultValue = "") String value3,
            @RequestParam(value="action",required =false, defaultValue = "") String action,
            @RequestParam(value="description",required =false) String description) {
        logger.info("/precondition/deleteparameter GET 方法被调用!!");
        PreconditionBean preconditionBean=new PreconditionBean();
        preconditionBean.settype(type);
        preconditionBean.setvalue(value);
        preconditionBean.setvalue2(value2);
        preconditionBean.setvalue3(value3);
        preconditionBean.setdescription(description);
        preconditionBean.setvalue1(value1);
        preconditionBean.setaction(action);

        if( preId!=-1) {
            preconditionBean.setid( preId);
            preconditionMapper.updatePrecondition(preconditionBean);
        }
        else
            preconditionMapper.insertPrecondition(preconditionBean);
        Result<String> result = new Result<>();

        result.setResult("操作成功");
        result.setSuccess(true);
        return result;
    }
}
