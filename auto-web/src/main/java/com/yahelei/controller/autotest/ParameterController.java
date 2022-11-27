package com.yahelei.controller.autotest;

import com.yahelei.dao.mysql.autotest.ParameterMapper;
import com.yahelei.domain.autotest.ParameterBean;
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
@RequestMapping("/yahelei/parameter")
public class ParameterController {
    @Resource
    public ParameterMapper parameterMapper;
    private static final Logger logger = LoggerFactory.getLogger(ParameterController.class);
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<?> list(@RequestParam(value="name",required =false,defaultValue = "") String name,
                          @RequestParam(value="description",required =false,defaultValue = "") String description)
    {
        logger.info("/parameter/list GET 方法被调用!!");
        List<ParameterBean> list =parameterMapper.getParameterList("%"+name+"%","%"+description+"%");
        Result<List<ParameterBean>> result = new Result<>();

        result.setResult(list);
        result.setSuccess(true);
        return result;
    }
    @RequestMapping(value = "/deleteparameter", method = RequestMethod.GET)
    @ResponseBody
    public Result<?> deleteparameter(@RequestParam(value="parameterId", defaultValue = "-1") Integer parameterId) {
        logger.info("/parameter/deleteproject GET 方法被调用!!");
        parameterMapper.deleteParameter(parameterId);
        Result<String> result = new Result<>();

        result.setResult("删除成功");
        result.setSuccess(true);
        return result;
    }
    @RequestMapping(value = "/addoretidparameter", method = RequestMethod.GET)
    @ResponseBody
    public Result<?> addoretidparameter(
            @RequestParam(value="parameterId", defaultValue = "-1") Integer  parameterId,
            @RequestParam(value="name") String name,
            @RequestParam(value="value" ,required =false) String value,
            @RequestParam(value="description") String description) {
        logger.info("/parameter/addoretidparameter GET 方法被调用!!");
        ParameterBean parameterBean=new ParameterBean();
        parameterBean.setname(name);
        parameterBean.setvalue(value);
        parameterBean.setdescription(description);
        List<ParameterBean> x= parameterMapper.getParameterListbyname(name);
        if( parameterId!=-1) {
            if(x.size()>0 &&x.get(0).getid()!=parameterId)
            {
                Result<String> result = new Result<>();
                result.setMessage("操作失败，该参数已经存在请使用");
                result.setSuccess(false);
                result.setCode(-1);
                return result;
            }
            parameterBean.setid( parameterId);
            parameterMapper.updateParameter(parameterBean);
        }
        else {
            if(x.size()>0)
            {
                Result<String> result = new Result<>();
                result.setMessage("操作失败，该参数已经存在请使用");
                result.setSuccess(false);
                result.setCode(-1);
                return result;
            }
            parameterMapper.insertParameter(parameterBean);
        }
        Result<String> result = new Result<>();
        result.setResult("操作成功");
        result.setSuccess(true);
        return result;
    }
}
