package com.yahelei.controller.autotest;

import com.yahelei.dao.mysql.autotest.CaseMapper;
import com.yahelei.dao.mysql.autotest.InterfaceMapper;
import com.yahelei.dao.mysql.autotest.LabelMapper;
import com.yahelei.dao.mysql.autotest.ProjectMapper;
import com.yahelei.domain.autotest.CaseBean;
import com.yahelei.domain.autotest.LabelBean;
import com.yahelei.resentity.Result;
import com.yahelei.service.autotest.CaseService;
import com.yahelei.service.autotest.impl.CaseImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.AddCommand;
import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/yahelei/case")
@Slf4j
public class CaseController {
    @Resource
    public ProjectMapper projectMapper;
    @Resource
    public LabelMapper labelMapper;
    @Resource
    public InterfaceMapper interfaceMapper;
    @Resource
    public CaseMapper caseMapper;
    @Resource
    public CaseService caseService;
    @Resource
    public CaseImpl caseImpl;
    @GetMapping(value = "/getcaselistbydescription")
    public Result<?> getcaselistbydescription(
            @RequestParam(name = "Interface_id", defaultValue = "") String Interface_id,
            @RequestParam(name = "project_id", defaultValue = "") String project_id,
            @RequestParam(name = "description", defaultValue = "") String description
    ) {
            Result<List<CaseBean>> result = new Result();
            List<CaseBean> a = caseMapper.getcaselistbydescription(description,Interface_id,project_id);
            for(CaseBean c:a)
            {
                c.setbody(caseImpl.evalParameter(c.getbody()));
                c.setheader(caseImpl.evalParameter(c.getheader()));
                c.setexpect(caseImpl.evalParameter(c.getexpect()));
                c.setpostcondition(caseImpl.evalParameter(c.getpostcondition()));
            }
            result.setResult(a);
            result.setSuccess(true);
            return result;
    }
    @GetMapping(value = "/getcaselistbyInterface_id")
    public Result<?> getcaselistbyInterface_id(@RequestParam(name = "Interface_id", defaultValue = "") String Interface_id,
                                               @RequestParam(name = "project_id", defaultValue = "") String project_id
    ){
        Result<List<CaseBean>> result = new Result();
        List<CaseBean> a = caseMapper.getcaselistbyInterface_id(Interface_id,project_id);
        for(CaseBean c:a)
        {
            String labellist=c.getlabellist();
            if(labellist.length()>0){
                String[] labellist2= labellist.split(",");
                String labellistresult="";
                for(String labellist3:labellist2)
                {
                    labellistresult=labellistresult+labelMapper.getdetailbyid(labellist3).getdescription()+",";
                }
                c.setlabellist(labellistresult.substring(0,labellistresult.length()-1));
            }
            if(c.getbody_type2().equals("0"))
                c.setbody(caseImpl.evalParameter(c.getbody()));
            else if(c.getbody_type2().equals("1"))
                c.setbody(caseImpl.evalParameter(c.getbody2()));
            c.setheader(caseImpl.evalParameter(c.getheader()));
            c.setexpect(caseImpl.evalParameter(c.getexpect()));
            c.setpostcondition(caseImpl.evalParameter(c.getpostcondition()));
        }
        result.setResult(a);
        result.setSuccess(true);
        return result;
    }
    @GetMapping(value = "/copy")
    public Result<?> copycase(@RequestParam(name = "id", defaultValue = "") String id
    ) throws Exception {
        Result<Boolean> result = new Result();
        CaseBean c = caseMapper.getdetailbyid(id);
        caseMapper.insertcase(c);
        result.setSuccess(true);
        return result;
    }
    @RequestMapping(value = "downloadFile",method = RequestMethod.GET)
    public ResponseEntity<byte[]> downloadFile(@RequestParam(name = "filepath", defaultValue = "") String filepath
    ) throws IOException {
        String path = filepath;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(new File(path)), headers, HttpStatus.CREATED);
    }
    @GetMapping(value = "downloadRisk")
    public void downloadRisk(
            @RequestParam(name = "fileurl", defaultValue = "") String fileurl,
                HttpServletResponse response) throws IOException {
        try {
            String path = fileurl;
            File file = new File(path);
            InputStream fis;
            fis = new BufferedInputStream(new FileInputStream(path));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            fis.close();
            response.reset();
            String fileName = URLEncoder.encode(fileurl.substring(fileurl.lastIndexOf("/")+1,fileurl.length()),"UTF-8");
            response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.addHeader("Content-Length", "" + file.length());
            OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            toClient.write(buffer);
            toClient.flush();
            toClient.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    @GetMapping(value = "/getdetailbyid")
    public Result<?> getdetailbyid(@RequestParam(name = "caseid", defaultValue = "") String caseid,
                                   @RequestParam(name = "project_id", defaultValue = "") String project_id,
                                   @RequestParam(name = "Interface_id", defaultValue = "") String Interface_id) throws Exception {
        CaseBean a=new CaseBean();
        Result<CaseBean> result = new Result();
        List<LabelBean> y=labelMapper.getLabelList(project_id,"");

        if(caseid.equals("-1"))
        {
            a=caseMapper.getcaseaddinfo(project_id,Interface_id);
            a.setx(y);
        }
        else {
            a = caseMapper.getdetailbyid(caseid);
            a.setx(y);
        }
        a.setInterface_id(Interface_id);
        result.setResult(a);
        result.setSuccess(true);
        return result;
    }
    @PostMapping(value = "/executedebug")
    public Result<?> executedebug(@RequestParam(name = "http", defaultValue = "") String http,
                                  @RequestParam(name = "host", defaultValue = "") String host,
                                  @RequestParam(name = "httpend", defaultValue = "") String httpend,
                                  @RequestParam(name = "project_id", defaultValue = "") String project_id,
                                  @RequestParam(name = "header", defaultValue = "") String header,
                                  @RequestParam(name = "url", defaultValue = "") String url,
                                  @RequestParam(name = "body", defaultValue = "") String body,
                                  @RequestParam(name = "signtype", defaultValue = "") String signtype,
                                  @RequestParam(name = "sign", defaultValue = "") String sign,
                                  @RequestParam(name = "sqlid", defaultValue = "") String sqlid,
                                  @RequestParam(name = "body2", defaultValue = "") String body2,
                                  @RequestParam(name = "expect_sql", defaultValue = "") String expect_sql,
                                  @RequestParam(name = "body_type", defaultValue = "") String body_type,
                                  @RequestParam(name = "body_type2", defaultValue = "") String body_type2,
                                  @RequestParam(name = "expect", defaultValue = "") String expect,
                                  @RequestParam(name = "postcondition", defaultValue = "") String postcondition,
                                  @RequestParam(name = "precondition", defaultValue = "") String precondition,
                                  @RequestParam(name = "method", defaultValue = "") String method
    ) throws Exception {
        Result<CaseBean> result = new Result();
        CaseBean c=new CaseBean();
        c.setproject_id(project_id);
        c.sethost(host);
        c.sethttp(http);
        c.seturl(url);
        c.setheader(header);
        c.sethttpend(httpend);
        c.setbody_type(body_type);
        c.setbody_type2(body_type2);
        c.setsign(sign);
        c.setsigntype(signtype);
        c.setbody(body);
        c.setbody2(body2);
        c.setexpect(expect);
        c.setpostcondition(postcondition);
        c.setprecondition(precondition);
        c.setmethod(method);
        c.setsqlid(sqlid);
        c.setexpect_sql(expect_sql);
        String x=caseService.execute(c);
        c.setResponseResult(x+c.getResponseResult());
        result.setResult(c);
        result.setSuccess(true);
        return result;
    }
    @GetMapping(value = "/executecasebycaseid")
    public Result<?> executecasebycaseid(@RequestParam(name = "caseid", defaultValue = "") String caseid
    ) throws Exception {
        Result<String> result = new Result();
        String result1=caseService.executecaseid(caseid);
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

    @PostMapping(value = "/updateoraddcase")
    public Result<?> updateoraddcase(
                                  @RequestParam(name = "header", defaultValue = "") String header,
                                  @RequestParam(name = "body", defaultValue = "") String body,
                                  @RequestParam(name = "signtype", defaultValue = "") String signtype,
                                  @RequestParam(name = "sign", defaultValue = "") String sign,
                                  @RequestParam(name = "body_type", defaultValue = "") String body_type,
                                  @RequestParam(name = "body_type2", defaultValue = "") String body_type2,
                                  @RequestParam(name = "expect", defaultValue = "") String expect,
                                  @RequestParam(name = "postcondition", defaultValue = "") String postcondition,
                                  @RequestParam(name = "precondition", defaultValue = "") String precondition,
                                  @RequestParam(name = "body2", defaultValue = "") String body2,
                                  @RequestParam(name = "sqlid", defaultValue = "") String sqlid,
                                  @RequestParam(name = "expect_sql", defaultValue = "") String expect_sql,
                                  @RequestParam(name = "labellist", defaultValue = "") String labellist,
                                  @RequestParam(name = "description", defaultValue = "") String description,
                                  @RequestParam(name = "project_id", defaultValue = "") String project_id,
                                  @RequestParam(name = "httpend", defaultValue = "") String httpend,
                                  @RequestParam(name = "case_id", defaultValue = "-1") int case_id,
                                  @RequestParam(name = "Interface_id", defaultValue = "") String Interface_id
    ) throws Exception {
        Result<Integer> result = new Result();
        CaseBean c=new CaseBean();
        c.setheader(header);
        c.setsqlid(sqlid);
        c.setexpect_sql(expect_sql);
        c.setbody(body);
        c.setbody_type(body_type);
        c.setbody_type2(body_type2);
        c.setsign(sign);
        c.sethttpend(httpend);
        c.setsigntype(signtype);
        c.setexpect(expect);
        c.setbody2(body2);
        c.setpostcondition(postcondition);
        c.setprecondition(precondition);
        c.setlabellist(labellist);
        c.setproject_id(project_id);
        c.setInterface_id(Interface_id);
        c.setdescription(description);
        if(case_id!=-1)
        {
            c.setid(case_id);
            caseService.reduceLabel(case_id);
            caseMapper.updatecase(c);
        }
        else {
            caseMapper.insertcase(c);
            case_id=c.getid();
        }
        caseService.addlabellist(labellist,case_id);
        result.setResult(case_id);
        result.setSuccess(true);
        return result;
    }
    @RequestMapping(value = "/deletecase", method = RequestMethod.GET)
    @ResponseBody
    public Result<?> deletecase(@RequestParam(value="id", defaultValue = "-1") Integer id
    ) {
        caseService.reduceLabel(id);
        caseMapper.deletecase(id);
        Result<String> result = new Result<>();

        result.setResult("删除成功");
        result.setSuccess(true);
        return result;
    }
}

