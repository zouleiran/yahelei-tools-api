package com.yahelei.controller.autotest;

import com.yahelei.dao.mysql.autotest.LabelMapper;
import com.yahelei.domain.autotest.LabelBean;
import com.yahelei.resentity.Result;
import com.yahelei.service.autotest.LabelService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/yahelei/Label")
@Slf4j
public class LabelController {
    @Resource
    public LabelMapper labelMapper;
    @Resource
    public LabelService labelService;
    private static final Logger logger = LoggerFactory.getLogger(LabelController.class);
    @GetMapping(value = "/getlist")
    public Result<?> list() throws Exception {
        logger.info("/Label/list GET 方法被调用!!");
        Result<List<LabelBean>> result = new Result();
        List<LabelBean> a = labelMapper.getLabellist2();
        result.setResult(a);
        result.setSuccess(true);
        return result;
    }
    @GetMapping(value = "/list")
    public Result<?> list(@RequestParam(name = "projectId", defaultValue = "") String project_id,
                          @RequestParam(name = "description", defaultValue = "") String description) throws Exception {
        logger.info("/Label/list GET 方法被调用!!");
        Result<List<LabelBean>> result = new Result();
        List<LabelBean> a = labelService.getLabelList(project_id,description);
        result.setResult(a);
        result.setSuccess(true);
        return result;
    }
    @RequestMapping(value = "/deleteLabel", method = RequestMethod.GET)
    @ResponseBody
    public Result<?> deleteInterface(@RequestParam(value="LabelId", defaultValue = "-1") Integer LabelId) {
        logger.info("/Interface/deleteInterface GET 方法被调用!!");
        labelMapper.deleteLabel(LabelId);
        Result<String> result = new Result<>();

        result.setResult("删除成功");
        result.setSuccess(true);
        return result;
    }
    @RequestMapping(value = "/addoretidLabel", method = RequestMethod.GET)
    @ResponseBody
    public Result<?> addoretidInterface(
            @RequestParam(value="projectId") String projectId,
            @RequestParam(value="LabelId") String LabelId,
            @RequestParam(value="paramlabel",defaultValue = "") String paramlabel,
            @RequestParam(value="host",defaultValue = "") String host,
            @RequestParam(value="description") String description,
            @RequestParam(value="caselist") String caselist
    ) {
        logger.info("/Label/addoretidLabel GET 方法被调用!!");
        LabelBean labelBean=new LabelBean();
        labelBean.setProjectId(projectId);
        labelBean.setparamlabel(paramlabel);
        labelBean.sethost(host);
        labelBean.setdescription(description);
        labelBean.setcaselist(caselist);
        labelBean.setcaseCount(caselist.length()>0?caselist.split(",").length:0);
        if(LabelId!=null&&LabelId.length()!=0&&!LabelId.equals("-1")) {
            labelBean.setid(Integer.parseInt(LabelId));
            labelService.updatelabel(labelBean);
        }
        else {
            labelService.insertlabel(labelBean);
        }
        Result<String> result = new Result<>();

        result.setResult("操作成功");
        result.setSuccess(true);
        return result;
    }
    @GetMapping(value = "/executebylabelid")
    public Result<?> executebylabelid(@RequestParam(name = "Labelid", defaultValue = "") String Labelid
    ) throws Exception {
        Result<String> result = new Result();
        String result1=labelService.executebylabelid(Labelid);
        if (result1.indexOf("FAIL")==-1) {
            result.setResult(result1);
            result.setSuccess(true);
        }
        else
        {
            result.setResult(result1);
            result.setSuccess(false);
        }
        return result;
    }
}

