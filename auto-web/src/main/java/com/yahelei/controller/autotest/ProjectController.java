package com.yahelei.controller.autotest;

import com.yahelei.dao.mysql.autotest.InterfaceMapper;
import com.yahelei.dao.mysql.autotest.ProjectMapper;
import com.yahelei.domain.autotest.ProjectBean;
import com.yahelei.resentity.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/yahelei/project")
public class ProjectController {
    @Resource
    public InterfaceMapper interfaceMapper;
    @Resource
    public ProjectMapper projectMapper;
    private static final Logger logger = LoggerFactory.getLogger(ProjectController.class);
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<?> list(@RequestParam(value="description",required =false) String description,
                          @RequestParam(value="host",required =false) String host
    )
    {
        logger.info("/project/list GET 方法被调用!!");
        List<ProjectBean> list =projectMapper.getProjectList(host,description);
        Result<List<ProjectBean>> result = new Result<>();

        result.setResult(list);
        result.setSuccess(true);
        return result;
    }
    @RequestMapping(value = "/getprojectbyid", method = RequestMethod.GET)
    @ResponseBody
    public Result<?> getprojectbyid(@RequestParam(value="projectId", defaultValue = "-1") Integer projectId) {
        logger.info("/project/getprojectbyid GET 方法被调用!!");
        ProjectBean projectBean =projectMapper.getProjectByprojectId(projectId);
        Result<ProjectBean> result = new Result<>();

        result.setResult(projectBean);
        result.setSuccess(true);
        return result;
    }
    @RequestMapping(value = "/deleteproject", method = RequestMethod.GET)
    @ResponseBody
    public Result<?> deleteproject(@RequestParam(value="projectId", defaultValue = "-1") Integer projectId) {
        logger.info("/project/deleteproject GET 方法被调用!!");
        Result<String> result = new Result<>();
        if(interfaceMapper.getInterfacelist("",projectId+"","").size()==0) {
            projectMapper.deleteproject(projectId);
            result.setResult("删除成功");
            result.setSuccess(true);
        }
        else {
            result.setResult("删除失败，该项目下还存在接口，需要先删除接口再删除项目");
            result.setSuccess(false);
        }
        return result;
    }
    @RequestMapping(value = "/addoretidproject", method = RequestMethod.GET)
    @ResponseBody
    public Result<?> addoretidproject(
            @RequestParam(value="projectId", defaultValue = "-1") Integer projectId,
            @RequestParam(value="host") String host,
            @RequestParam(value="http") String http,
            @RequestParam(value="common_body") String common_body,
            @RequestParam(value="common_header") String common_header,
            @RequestParam(value="description") String description) {
        logger.info("/project/addoretidproject GET 方法被调用!!");
        ProjectBean projectBean=new ProjectBean();
        projectBean.sethttp(http);
        projectBean.sethost(host);
        projectBean.setcommon_header(common_header);
        projectBean.setcommon_body(common_body);
        projectBean.setdescription(description);
        List<ProjectBean> x=projectMapper.getProjectListequal(host);
        if(projectId!=-1) {
            if(x.size()>0 &&x.get(0).getid()!=projectId)
            {
                Result<String> result = new Result<>();
                result.setMessage("操作失败，该服务已经存在请使用");
                result.setSuccess(false);
                result.setCode(-1);
                return result;
            }
            projectBean.setid(projectId);
            projectMapper.updateproject(projectBean);
        }
        else {
            if(x.size()>0)
            {
                Result<String> result = new Result<>();
                result.setMessage("操作失败，该服务已经存在请使用");
                result.setSuccess(false);
                result.setCode(-1);
                return result;
            }
            projectMapper.insertproject(projectBean);
        }
        Result<String> result = new Result<>();

        result.setResult("操作成功");
        result.setSuccess(true);
        return result;
    }
}
