package com.yahelei.controller.autotest;

import com.yahelei.dao.mysql.autotest.*;
import com.yahelei.domain.autotest.*;
import com.yahelei.resentity.Result;
import com.yahelei.service.autotest.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/yahelei/task")
public class TaskController{
    @Resource
    public TaskMapper taskMapper;
    @Resource
    public TaskService taskService;
    private static final Logger logger = LoggerFactory.getLogger(TaskController.class);
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<?> list(@RequestParam(value = "description", required = false) String description,
                          @RequestParam(value = "type", required = false) String type
    ) {
        logger.info("/task/list GET 方法被调用!!");
        List<TaskBean> list = taskService.gettaskList(type,description);
        Result<List<TaskBean>> result = new Result<>();
        result.setResult(list);
        result.setSuccess(true);
        return result;
    }

    @RequestMapping(value = "/getlist", method = RequestMethod.GET)
    public Result<?> getlist(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "project_id", required = false) String project_id
    ) {
        logger.info("/task/getlist GET 方法被调用!!");
        List<A> list = taskService.getList(type,project_id);
        Result<List<A>> result = new Result<>();
        result.setResult(list);
        result.setSuccess(true);
        return result;
    }
    @RequestMapping(value = "/deletetask", method = RequestMethod.GET)
    public Result<?> deletetask(
            @RequestParam(value = "id", required = false) int id
    ) {
        logger.info("/task/deletetask GET 方法被调用!!");
        taskMapper.deletetask(id);
        Result<String> result = new Result<>();
        result.setResult("删除成功");
        result.setSuccess(true);
        return result;
    }
    @RequestMapping(value = "/addoretidtask", method = RequestMethod.GET)
    @ResponseBody
    public Result<?> addoretidtask(
            @RequestParam(value="id", defaultValue = "-1") Integer id,
            @RequestParam(value="projectId") String projectId,
            @RequestParam(value="host", required = false) String host,
            @RequestParam(value="type", required = false) String type,
            @RequestParam(value="value", required = false) String value,
            @RequestParam(value="email", required = false) String email,
            @RequestParam(value="common_header", required = false) String common_header,
            @RequestParam(value="param", required = false) String param,
            @RequestParam(value="description", required = false) String description
    ){
        logger.info("/task/addoretidtask GET 方法被调用!!");
        TaskBean taskBean=new TaskBean();
        taskBean.settype(type);
        taskBean.sethost(host);
        taskBean.setparam(param);
        taskBean.setcommon_header(common_header);
        taskBean.setprojectId(projectId);
        taskBean.setvalue(value);
        taskBean.setemail(email);
        taskBean.setdescription(description);
        if (id != -1) {
            taskBean.setid(id);
            taskMapper.updatetask(taskBean);
        } else {
            taskMapper.inserttask(taskBean);
        }
        Result<String> result = new Result<>();
        result.setResult("操作成功");
        result.setSuccess(true);
        return result;
    }
    @RequestMapping(value = "/executebytaskid", method = RequestMethod.GET)
    @ResponseBody
    public Result<?> executebytaskid(
            @RequestParam(value="taskId") Integer taskId,
            @RequestParam(value="host", required = false,defaultValue = "") String host
    ) {
        logger.info("/task/executebytaskid GET 方法被调用!!");
        Result<String> result = new Result();
        if (taskService.executetask(taskId,host)) {
            result.setResult("成功");
            result.setSuccess(true);
        }else
        {
            result.setResult("失败");
            result.setSuccess(false);
        }
        return result;
    }
}
