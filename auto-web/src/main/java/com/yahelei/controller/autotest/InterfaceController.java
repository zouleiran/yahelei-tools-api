package com.yahelei.controller.autotest;

import com.yahelei.dao.mysql.autotest.CaseMapper;
import com.yahelei.dao.mysql.autotest.InterfaceMapper;
import com.yahelei.domain.autotest.InterfaceBean;
import com.yahelei.resentity.Result;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/yahelei/Interface")
@Slf4j
public class InterfaceController {
    @Resource
    public InterfaceMapper interfaceMapper;
    @Resource
    public CaseMapper caseMapper;
    private static final Logger logger = LoggerFactory.getLogger(InterfaceController.class);
    @GetMapping(value = "/list")
    public Result<?> list(@RequestParam(name = "url", defaultValue = "",required =false) String url,
                          @RequestParam(name = "project_id", defaultValue = "",required =false) String project_id,
                          @RequestParam(name = "description", defaultValue = "",required =false) String description) throws Exception {
        logger.info("/Interface/list GET 方法被调用!!");
        Result<List<InterfaceBean>> result = new Result();
        List<InterfaceBean> a = interfaceMapper.getInterfacelist(url,project_id,description);
        for(InterfaceBean x:a)
        {
            x.setnum(caseMapper.getcasenum(x.getid()));
        }
        result.setResult(a);
        result.setSuccess(true);
        return result;
    }
    @GetMapping(value = "/getdetailbyInterfaceid")
    public Result<?> list(
                          @RequestParam(name = "Interfaceid", defaultValue = "",required =false) String Interfaceid) throws Exception {
        logger.info("/Interface/getdetailbyInterfaceid GET 方法被调用!!");
        Result<InterfaceBean> result = new Result();
        InterfaceBean a = interfaceMapper.getdetailbyInterfaceid(Interfaceid);
        result.setResult(a);
        result.setSuccess(true);
        return result;
    }
    @RequestMapping(value = "/deleteInterface", method = RequestMethod.GET)
    @ResponseBody
    public Result<?> deleteInterface(
                                     @RequestParam(value="InterfaceId", defaultValue = "-1") Integer InterfaceId) {
        logger.info("/Interface/deleteInterface GET 方法被调用!!");
        interfaceMapper.deleteInterface(InterfaceId);
        Result<String> result = new Result<>();

        result.setResult("删除成功");
        result.setSuccess(true);
        return result;
    }
    @RequestMapping(value = "/addoretidInterface", method = RequestMethod.GET)
    @ResponseBody
    public Result<?> addoretidInterface(
            @RequestParam(value="InterfaceId", defaultValue = "-1") Integer InterfaceId,
            @RequestParam(value="url") String url,
            @RequestParam(value="method") String method,
            @RequestParam(value="projectId") String projectId,
            @RequestParam(value="description") String description) {
        logger.info("/Interface/addoretidInterface GET 方法被调用!!");
        InterfaceBean interfaceBean=new InterfaceBean();
        interfaceBean.setmethod(method);
        interfaceBean.setproject_id(projectId);
        interfaceBean.seturl(url);
        interfaceBean.setdescription(description);
        if(InterfaceId!=-1) {
            List<InterfaceBean> x = interfaceMapper.getInterfacelistequal(url, projectId);
            if(x.size()>0&&x.get(0).getid()!=InterfaceId)
            {
                Result<String> result = new Result<>();
                result.setMessage("操作失败，该接口已经存在");
                result.setSuccess(false);
                result.setCode(-1);
                return result;
            }
            interfaceBean.setid(InterfaceId);
            interfaceMapper.updateInterface(interfaceBean);
        }
        else {
            if(interfaceMapper.getInterfacelistequal(url,projectId).size()>0)
            {
                Result<String> result = new Result<>();
                result.setMessage("操作失败，该接口已经存在");
                result.setSuccess(false);
                result.setCode(-1);
                return result;
            }
            interfaceMapper.insertInterface(interfaceBean);
        }
        Result<String> result = new Result<>();
        result.setResult("操作成功");
        result.setSuccess(true);
        return result;
    }
}

