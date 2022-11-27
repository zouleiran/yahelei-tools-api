package com.yahelei.service.autotest.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yahelei.utils.sslzlr;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import com.yahelei.dao.mysql.autotest.*;
import com.yahelei.domain.autotest.*;
import com.yahelei.service.autotest.CaseService;
import com.yahelei.service.autotest.ConnectionDB;
import com.yahelei.utils.HttpClientUtil;
import com.yahelei.utils.HttpsClientUtil;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.testng.annotations.Test;
import org.yaml.snakeyaml.Yaml;

import javax.annotation.Resource;
import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.lang.Thread.sleep;

@Service
public class CaseImpl implements CaseService {
    @Resource
    public CaseMapper caseMapper;
    @Resource
    public CaseExecuteLogMapper CaseExecuteLogMapper;
    @Resource
    public ParameterMapper parameterMapper;
    @Resource
    public PreconditionMapper preconditionMapper;
    @Resource
    public AddressMapper addressMapper;
    @Resource
    public LabelMapper labelMapper;
    @Resource
    public ProjectMapper projectMapper;

    @Override
    public boolean executecaseid(String caseid, int taskid, long SerialId,
                                  String host) {
        return false;
    }
    @Override
    public boolean execute2(String caseid, int taskid, String Labelid, long SerialId
            ,  String host, Map parameter) {
        System.out.println("执行用例编号" + caseid);
        CaseBean a = caseMapper.getdetailbyid(caseid);
        CaseExecuteLogBean caseExecutelogbean = new CaseExecuteLogBean();
        caseExecutelogbean.setPath(a.geturl());
        caseExecutelogbean.setCaseId(parseInt(caseid));
        caseExecutelogbean.setProjectId(a.getproject_id());
        caseExecutelogbean.setenv(a.getenv());
        caseExecutelogbean.setInterfaceId(a.getInterface_id());
        caseExecutelogbean.setExpectedResult(a.getexpect());
        caseExecutelogbean.setSerialId(SerialId);
        caseExecutelogbean.setTaskId(taskid);
        caseExecutelogbean.setlabelId(Labelid);
        try {
            String des = execute(a,host,parameter);
            caseExecutelogbean.setExecTime(a.getExecTime());
            caseExecutelogbean.setresult(a.getResult());
            if (a.getbody() != null && a.getbody().length() > 500)
                caseExecutelogbean.setParameters(a.getbody().substring(0, 500));
            else
                caseExecutelogbean.setParameters(a.getbody());

            if (a.geturl() != null && a.geturl().length() > 500)
                caseExecutelogbean.setUrl(a.geturl().substring(0, 500));
            else
                caseExecutelogbean.setUrl(a.geturl());

            if (a.getResponseResult() != null && a.getResponseResult().length() > 1000)
                caseExecutelogbean.setResponseResult(a.getResponseResult().substring(0, 1000));
            else
                caseExecutelogbean.setResponseResult(a.getResponseResult());
            caseExecutelogbean.setheader(a.getheader());
            caseExecutelogbean.setprecondition(a.getprecondition());
            caseExecutelogbean.setdescription(des);
            CaseExecuteLogMapper.add(caseExecutelogbean);
            return a.getResult();
        } catch (Exception e) {
            String des = "FAIL整体执行失败";
            caseExecutelogbean.setresult(false);
            caseExecutelogbean.setdescription(des);
            if (a.getbody() != null && a.getbody().length() > 500)
                caseExecutelogbean.setParameters(a.getbody().substring(0, 500));
            else
                caseExecutelogbean.setParameters(a.getbody());

            if (a.geturl() != null && a.geturl().length() > 500)
                caseExecutelogbean.setUrl(a.geturl().substring(0, 500));
            else
                caseExecutelogbean.setUrl(a.geturl());
            caseExecutelogbean.setheader(a.getheader());
            caseExecutelogbean.setprecondition(a.getprecondition());
            CaseExecuteLogMapper.add(caseExecutelogbean);
            return false;
        }
    }
    public String execute(CaseBean caseBean,String host, Map parameter) {
        System.out.println("执行用例"+host);
        long sTime = System.currentTimeMillis();
        String result = "";
        try {
            result = result + dobefore(caseBean);
            if (result.contains("FAIL")) {
                return result;
            }
            String res = "";
            if (caseBean.gethttp().equals("http"))
                res = dohttp(caseBean);
            else
                res = dohttps(caseBean);
            result = result + res;
            if (result.contains("FAIL")) {
                return result;
            }
            result = result + doexpect(caseBean) + doexpectsql(caseBean);
            if (result.contains("FAIL")) {
                return result;
            }
            result = result + dopostProcessing(caseBean);
            if (result.contains("FAIL"))
                caseBean.setResult(false);
            else
                caseBean.setResult(true);
            long a = System.currentTimeMillis() - sTime;
            caseBean.setExecTime(a);
            return result+"url为"+caseBean.geturl();
        } catch (Exception e) {
            return "FAIL整体执行异常，需要排查";
        }
    }
    @Override
    public boolean executecaseid(String caseid, int taskid, String Labelid, long SerialId) {
        System.out.println("执行用例编号" + caseid);
        CaseBean a = caseMapper.getdetailbyid(caseid);
        CaseExecuteLogBean caseExecutelogbean = new CaseExecuteLogBean();
        caseExecutelogbean.setPath(a.geturl());
        caseExecutelogbean.setCaseId(parseInt(caseid));
        caseExecutelogbean.setProjectId(a.getproject_id());
        caseExecutelogbean.setenv(a.getenv());
        caseExecutelogbean.setInterfaceId(a.getInterface_id());
        caseExecutelogbean.setExpectedResult(a.getexpect());
        caseExecutelogbean.setSerialId(SerialId);
        caseExecutelogbean.setTaskId(taskid);
        caseExecutelogbean.setlabelId(Labelid);
        try {
            String des = execute(a);
            caseExecutelogbean.setExecTime(a.getExecTime());
            caseExecutelogbean.setresult(a.getResult());
            if (a.getbody() != null && a.getbody().length() > 500)
                caseExecutelogbean.setParameters(a.getbody().substring(0, 500));
            else
                caseExecutelogbean.setParameters(a.getbody());

            if (a.geturl() != null && a.geturl().length() > 500)
                caseExecutelogbean.setUrl(a.geturl().substring(0, 500));
            else
                caseExecutelogbean.setUrl(a.geturl());

            if (a.getResponseResult() != null && a.getResponseResult().length() > 1000)
                caseExecutelogbean.setResponseResult(a.getResponseResult().substring(0, 1000));
            else
                caseExecutelogbean.setResponseResult(a.getResponseResult());
            caseExecutelogbean.setheader(a.getheader());
            caseExecutelogbean.setprecondition(a.getprecondition());
            caseExecutelogbean.setdescription(des);
            CaseExecuteLogMapper.add(caseExecutelogbean);
            return a.getResult();
        } catch (Exception e) {
            String des = "FAIL整体执行失败";
            caseExecutelogbean.setresult(false);
            caseExecutelogbean.setdescription(des);
            if (a.getbody() != null && a.getbody().length() > 500)
                caseExecutelogbean.setParameters(a.getbody().substring(0, 500));
            else
                caseExecutelogbean.setParameters(a.getbody());

            if (a.geturl() != null && a.geturl().length() > 500)
                caseExecutelogbean.setUrl(a.geturl().substring(0, 500));
            else
                caseExecutelogbean.setUrl(a.geturl());
            caseExecutelogbean.setheader(a.getheader());
            caseExecutelogbean.setprecondition(a.getprecondition());
            CaseExecuteLogMapper.add(caseExecutelogbean);
            return false;
        }
    }

    @Override
    public String executecaseid(String caseid) throws Exception {
        System.out.println("执行用例编号" + caseid);
        CaseBean a = caseMapper.getdetailbyid(caseid);
        CaseExecuteLogBean caseExecutelogbean = new CaseExecuteLogBean();
        caseExecutelogbean.setPath(a.geturl());
        caseExecutelogbean.setSerialId(System.currentTimeMillis() / 1000);
        caseExecutelogbean.setCaseId(parseInt(caseid));
        caseExecutelogbean.setProjectId(a.getproject_id());
        caseExecutelogbean.setenv(a.getenv());
        caseExecutelogbean.setInterfaceId(a.getInterface_id());
        caseExecutelogbean.setTaskId(-1);
        caseExecutelogbean.setSerialId(-1);
        caseExecutelogbean.setExpectedResult(a.getexpect());
        try {
            String x = execute(a);
            caseExecutelogbean.setExecTime(a.getExecTime());
            caseExecutelogbean.setresult(a.getResult());
            if (a.getbody() != null && a.getbody().length() > 500)
                caseExecutelogbean.setParameters(a.getbody().substring(0, 500));
            else
                caseExecutelogbean.setParameters(a.getbody());
            if (a.geturl() != null && a.geturl().length() > 500)
                caseExecutelogbean.setUrl(a.geturl().substring(0, 500));
            else
                caseExecutelogbean.setUrl(a.geturl());

            if (a.getResponseResult() != null && a.getResponseResult().length() > 1000)
                caseExecutelogbean.setResponseResult(a.getResponseResult().substring(0, 1000));
            else
                caseExecutelogbean.setResponseResult(a.getResponseResult());
            caseExecutelogbean.setheader(a.getheader());
            caseExecutelogbean.setprecondition(a.getprecondition());
            caseExecutelogbean.setdescription(x);
            caseExecutelogbean.setlabelId("-1");
            CaseExecuteLogMapper.add(caseExecutelogbean);
            return x;
        } catch (Exception e) {
            String des = "FAIL整体执行失败";
            caseExecutelogbean.setresult(false);
            if (a.getbody() != null && a.getbody().length() > 500)
                caseExecutelogbean.setParameters(a.getbody().substring(0, 500));
            else
                caseExecutelogbean.setParameters(a.getbody());

            if (a.geturl() != null && a.geturl().length() > 500)
                caseExecutelogbean.setUrl(a.geturl().substring(0, 500));
            else
                caseExecutelogbean.setUrl(a.geturl());
            caseExecutelogbean.setlabelId("-1");
            caseExecutelogbean.setheader(a.getheader());
            caseExecutelogbean.setprecondition(a.getprecondition());
            caseExecutelogbean.setdescription(des);
            CaseExecuteLogMapper.add(caseExecutelogbean);
            return des;
        }
    }

    public String execute(CaseBean caseBean) {
        System.out.println("执行用例");
        long sTime = System.currentTimeMillis();
        String result = "";
        try {
            result = result + dobefore(caseBean);
            if (result.contains("FAIL")) {
                return result;
            }
            String res = "";
            if (caseBean.gethttp().equals("http"))
                res = dohttp(caseBean);
            else
                res = dohttps(caseBean);
            result = result + res;
            if (result.contains("FAIL")) {
                return result;
            }
            result = result + doexpect(caseBean) + doexpectsql(caseBean);
            if (result.contains("FAIL")) {
                return result;
            }
            result = result + dopostProcessing(caseBean);
            if (result.contains("FAIL"))
                caseBean.setResult(false);
            else
                caseBean.setResult(true);
            long a = System.currentTimeMillis() - sTime;
            caseBean.setExecTime(a);
            return result+"url为"+caseBean.geturl();
        } catch (Exception e) {
            return "FAIL整体执行异常，需要排查";
        }
    }
    @Override
    public void reduceLabel(int id){
        String x=caseMapper.getdetailbyid(id+"").getlabellist();
        if(x.length()>0) {
            String labellist1[] = caseMapper.getdetailbyid(id + "").getlabellist().split(",");
            for (int i = 0; i < labellist1.length; i++) {
                String[] labellist2 = labelMapper.getdetailbyid(labellist1[i]).getcaselist().split(",");
                StringBuffer sb = new StringBuffer();
                for (int j = 0; j < labellist2.length; j++)
                    if (!labellist2[j].equals(id+""))
                        sb.append(labellist2[j] + ",");
                    if(sb.length()>0)
                        labelMapper.reduceLabel(labellist1[i], sb.substring(0, sb.length() - 1).toString());
                    else
                        labelMapper.reduceLabel(labellist1[i],"");
            }
        }
    }

    @Override
    public void addlabellist(String labellist,int caseid) {
        if(labellist.length()>0) {
            String labellist1[] = labellist.split(",");
            for (int i = 0; i < labellist1.length; i++) {
                StringBuffer sb = new StringBuffer();
                String x = sb.append(caseid + "," + labelMapper.getdetailbyid(labellist1[i]).getcaselist()).toString();
                if (x.endsWith(","))
                    x = x.substring(0, x.length() - 1);
                labelMapper.addLabel(labellist1[i], x.toString());
            }
        }
    }

    private String dobefore(CaseBean caseBean) {
        if(caseBean.getprecondition().length()==2)
            return "该case不存在前置处理器，不需要执行ok<br>";
        JSONArray paramerMap = JSON.parseArray(caseBean.getprecondition());
        String result="";
        for(int i=0;i<paramerMap.size();i++) {
            String id=paramerMap.get(i)+"";
            PreconditionBean p=preconditionMapper.getPrecondition(id);
            result=result+"执行前置处理器编号"+id+"<br>";
            try {
                result=result+ dopreProcessing(p,caseBean.getproject_id());
            } catch (Exception e) {
                result=result+ "第"+i+"步FAIL执行有问题，请排查<br>";
            }
        }
        return caseBean.getid()+"的前置处理器执行ok"+":"+result+"<br>";
    }
    @Override
    public String dopreProcessing(PreconditionBean p,String projectId){
        String result="";
        try {
            if (p.gettype().equals("sqldeleteorupdate")) {
                System.out.println("执行sqldeleteorupdate,id为"+p.getid());
                String x = p.getvalue2();
                result = result + "执行sql,id为" + p.getid();
                AddressBean a = addressMapper.getAddressbyid(p.getvalue());
                ConnectionDB db = new ConnectionDB("com.mysql.jdbc.Driver", "jdbc:mysql://" + a.geturl() + ":" + a.getport() + "/m" +
                        "ysql?useUnicode=true&characterEncoding=utf-8", a.getusername(), a.getpassword());
                result = result + db.update(x) + "<br>";
            }
            else if (p.gettype().equals("sqlselect")) {
                System.out.println("执行sqlselect,id为"+p.getid());
                try {
                    AddressBean a=addressMapper.getAddressbyid(p.getvalue());
                    ConnectionDB db = new ConnectionDB("com.mysql.jdbc.Driver","jdbc:mysql://"+a.geturl()+":"+a.getport()
                            ,a.getusername(),a.getpassword());
                    Object result3=db.select2(evalParameter(p.getvalue2()));
                    ParameterBean parameterBean=new ParameterBean();
                    if(result3 instanceof Integer||result3 instanceof Long)
                        parameterBean.setvalue(result3.toString());
                    else
                        parameterBean.setvalue((String)result3);
                    parameterBean.setname(p.getvalue1());
                    if(parameterMapper.getParameterListbyname(p.getvalue1()).size()>0)
                    //更新数据
                        parameterMapper.updatevaluebyname(parameterBean);
                    else{
                        parameterBean.setdescription("系统自动创建"+p.getvalue1());
                        parameterMapper.insertParameter(parameterBean);
                    }
                    return result+"数据库操作sqlselect成功，"+evalParameter(p.getvalue2())+"<br>";
                } catch (Exception e) {
                    return result+"数据库断言FAIL<br>";
                }
            }
            else if (p.gettype().equals("sleep")) {
                sleep(Long.parseLong(p.getvalue1()));
                return result+"执行sleep,参数一为"+p.getvalue1()+"<br>";
            }
            else if (p.gettype().equals("code")) {
                System.out.println("执行代码,参数一为"+p.getvalue1());
                String p1=p.getvalue1();
                String class1="";
                String method1="";
                if(p1.lastIndexOf(".")==-1) {
                    class1="org.jeecg.modules.qatool.service.impl.A12Mock";
                    method1=p.getvalue1();
                }
                else
                {
                    class1="org.jeecg.modules.qatool.service.impl"+p1.substring(0,p1.lastIndexOf("."));
                    method1=p1.substring(p1.lastIndexOf(".")+1);
                }
                Class clz = Class.forName(class1);
                Method method = clz.getMethod(method1, String.class);
                Constructor constructor = clz.getConstructor();
                Object object = constructor.newInstance();
                String StringVal=method.invoke(object, p.getvalue2()).toString();
                if(p.getvalue3().length()!=0) {
                    ParameterBean p2=new ParameterBean();
                    p2.setvalue(StringVal);
                    p2.setname(p.getvalue3());
                    //更新数据
                    if(parameterMapper.getParameterListbyname(p.getvalue3()).size()>0)
                        //更新数据
                        parameterMapper.updatevaluebyname(p2);
                    else{
                        p2.setdescription("系统自动创建"+p.getvalue3());
                        parameterMapper.insertParameter(p2);
                    }
                }
                return result+"执行代码,参数一为"+p.getvalue1()+"<br>";
            }
            else if (p.gettype().equals("caseid")) {
                System.out.println("执行caseid,id为"+p.getvalue1());
                result = result + "执行caseid为"+p.getvalue1()+"<br>"+executecaseid(p.getvalue1()) + "<br>";
            }
        }
        catch (Exception e) {
            return result+"FAIL执行失败<br>";
        }
        return result;
    }
    public static String toUtf8(String str) throws UnsupportedEncodingException {
        String x= new String(str.getBytes("UTF-8"),"UTF-8");
        return x;
    }
    public static String toGBK(String str) throws UnsupportedEncodingException {
        String x= new String(str.getBytes("GBK"),"GBK");
        return x;
    }
    private String dohttps(CaseBean caseBean) {
        try {
            ProjectBean p=projectMapper.getProjectByprojectId(Integer.valueOf(caseBean.getproject_id()));
            String url = evalParameter(caseBean.gethttp() + "://" + caseBean.gethost() + caseBean.geturl());
            boolean flag=true;
            if (caseBean.getmethod().toUpperCase().equals("POST")&&caseBean.gethttpend().length()>0) {
                url = url + "?" + evalParameter(caseBean.gethttpend());
                flag=false;
            }
            if(p.getcommon_body().length()>2&&caseBean.getmethod().equals("POST"))
            {
                Map common_body = JSON.parseObject(evalParameter(p.getcommon_body()));
                if(flag)
                {
                    url=url+"?";
                    for(Object i2:common_body.keySet())
                        url = url + i2+"="+common_body.get(i2)+"&";
                    url=url.substring(0,url.length()-1);
                }
                else
                    for(Object i2:common_body.keySet())
                        url = url +"&"+i2+"="+common_body.get(i2);
            }

            String header = evalParameter(caseBean.getheader());
            if(caseBean.getbody_type().equals("2")){
                HttpClient httpClient = new sslzlr();
                HttpPost httpPost = new HttpPost(url);
                Map paramerMap3 = JSON.parseObject(caseBean.getbody());
                StringBuilder sb = new StringBuilder();
                String line;
                HttpResponse httpResponse = httpClient.execute(httpPost);
                InputStream inputStream = httpResponse.getEntity().getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                String rspMsg = URLDecoder.decode(sb.toString(),"UTF-8");
                caseBean.setResponseResult(rspMsg);
                return "接口请求成功<br>";
            }
            String body = evalParameter(caseBean.getbody());
            Map paramerMap = JSON.parseObject(body);
            Map headerMap = JSON.parseObject(header);

            if(p.getcommon_header().length()>2)
            {
                Map common_header = JSON.parseObject(evalParameter(p.getcommon_header()));
                for(Object common_headerkey:common_header.keySet())
                    headerMap.put(common_headerkey,common_header.get(common_headerkey));
            }
//            caseBean.seturl(url);
            String paramerString = null;
            if(caseBean.getbody_type2().equals("1"))
                paramerString=evalParameter(caseBean.getbody2());
            else {
                if (caseBean.getbody_type().equals("0")) {
                    StringBuffer sb = new StringBuffer();
                    for (Object x : paramerMap.keySet()) {
                        sb.append(x + "=" + paramerMap.get(x) + "&");
                    }
                    paramerString = sb.length() == 0 ? null : sb.toString().substring(0, sb.length() - 1);
                } else
                    paramerString = body.toString();
            }
            if(headerMap!=null)
                caseBean.setheader(headerMap.toString());
            caseBean.setbody(paramerString);
            System.out.println("发送请求url"+url);

            if((paramerString!=null&&paramerString.indexOf("${sign}")>=0)||url.indexOf("${sign}")>=0)
            {
                JSONArray signMap = JSON.parseArray(caseBean.getsign());
                String s="";
                for(int i=0;i<signMap.size();i++)
                    s=s+evalParameter(signMap.get(i).toString());
                if(caseBean.getsigntype().equals("md5"))
                    s=md5(s);
                else if(caseBean.getsigntype().equals("sha1"))
                    s=sha1(s);
                paramerString = paramerString.replace("${sign}", s);
                url=url.replace("${sign}", s);
            }
            if(p.getcommon_body().length()>2&&caseBean.getmethod().equals("GET"))
            {
                Map common_body = JSON.parseObject(evalParameter(p.getcommon_body()));
                for(Object i2:common_body.keySet())
                {
                    paramerString=paramerString+"&"+i2+"="+common_body.get(i2);
                }
                if(paramerString.startsWith("&"))
                    paramerString=paramerString.substring(1,paramerString.length());
            }
            caseBean.setbody2(paramerString);
            caseBean.setbody(paramerString);
            String result = HttpsClientUtil.sendhttps(url, paramerString, headerMap, caseBean.getmethod());
            caseBean.setResponseResult(result);
            return "接口请求成功<br>";
        } catch (Exception e) {
            return "接口请求失败<br>";
        }
    }

    private String dohttp(CaseBean caseBean) {
        try {
            ProjectBean p=projectMapper.getProjectByprojectId(Integer.valueOf(caseBean.getproject_id()));
            String url = evalParameter(caseBean.gethttp() + "://" + caseBean.gethost() + caseBean.geturl());
            boolean flag=true;
            if (caseBean.getmethod().toUpperCase().equals("POST")&&caseBean.gethttpend().length()>0) {
                url = url + "?" + evalParameter(caseBean.gethttpend());
                flag=false;
            }
            if(p.getcommon_body().length()>2&&caseBean.getmethod().equals("POST"))
            {
                Map common_body = JSON.parseObject(evalParameter(p.getcommon_body()));
                if(flag)
                {
                    url=url+"?";
                    for(Object i2:common_body.keySet())
                        url = url + i2+"="+common_body.get(i2)+"&";
                    url=url.substring(0,url.length()-1);
                }
                else
                    for(Object i2:common_body.keySet())
                        url = url +"&"+i2+"="+common_body.get(i2);
            }
            String header = evalParameter(caseBean.getheader());
            if(caseBean.getbody_type().equals("2")){//文件
                HttpClient httpClient = HttpClientBuilder.create().build();
                HttpPost httpPost = new HttpPost(url);
                Map paramerMap3 = JSON.parseObject(caseBean.getbody());
                JSONObject json = JSON.parseObject(caseBean.getheader());
                for(String key:json.keySet())
                    httpPost.setHeader(key,evalParameter(json.getString(key)));
                StringBuilder sb = new StringBuilder();
                String line;
                HttpResponse httpResponse = httpClient.execute(httpPost);
                InputStream inputStream = httpResponse.getEntity().getContent();
                BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }
                String rspMsg = URLDecoder.decode(sb.toString(),"UTF-8");
                caseBean.setResponseResult(rspMsg);
                return "接口请求成功<br>";
            }
                String body = evalParameter(caseBean.getbody());
                Map paramerMap = JSON.parseObject(body);
                Map headerMap = JSON.parseObject(header);
            if(p.getcommon_header().length()>2)
            {
                Map common_header = JSON.parseObject(evalParameter(p.getcommon_header()));
                for(Object common_headerkey:common_header.keySet())
                    headerMap.put(common_headerkey,common_header.get(common_headerkey));
            }
                caseBean.seturl(url);
                String paramerString = "";
                if(caseBean.getbody_type2().equals("1"))
                    paramerString=evalParameter(caseBean.getbody2());
                else {
                    if (caseBean.getbody_type().equals("0")) {
                        StringBuffer sb = new StringBuffer();
                        for (Object x : paramerMap.keySet()) {
                            sb.append(x + "=" + paramerMap.get(x) + "&");
                        }
                        paramerString = sb.length() == 0 ? "" : sb.toString().substring(0, sb.length() - 1);
                    } else
                        paramerString = body.toString();
                }
                if(headerMap!=null)
                    caseBean.setheader(headerMap.toString());
                caseBean.setbody(paramerString);
                System.out.println("发送请求url"+url);

                if((paramerString!=null&&paramerString.indexOf("${sign}")>=0)||url.indexOf("${sign}")>=0)
                {
                    JSONArray signMap = JSON.parseArray(caseBean.getsign());
                    String s="";
                    for(int i=0;i<signMap.size();i++)
                        s=s+evalParameter(signMap.get(i).toString());
                    if(caseBean.getsigntype().equals("md5"))
                        s=md5(s);
                    else if(caseBean.getsigntype().equals("sha1"))
                        s=sha1(s);
                    paramerString = paramerString.replace("${sign}", s);
                    url=url.replace("${sign}", s);
                }
            if(p.getcommon_body().length()>2&&caseBean.getmethod().equals("GET"))
            {
                Map common_body = JSON.parseObject(evalParameter(p.getcommon_body()));
                for(Object i2:common_body.keySet())
                {
                    paramerString=paramerString+"&"+i2+"="+common_body.get(i2);
                }
                if(paramerString.startsWith("&"))
                    paramerString=paramerString.substring(1,paramerString.length());
            }
                caseBean.setbody2(paramerString);
                caseBean.setbody(paramerString);
                String result = HttpClientUtil.sendhttp(url, paramerString, headerMap, caseBean.getmethod());
                caseBean.setResponseResult(result);
            return "接口请求成功<br>";
        } catch (Exception e) {
            return "接口请求失败<br>";
        }
    }
    public String md5(String input){
        if(input == null || input.equals("")){
            return "";
        }

        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        byte[] bytes = digest.digest(input.getBytes());
//        return byteArrayToHexString(bytes).toString();
        return "";
    }
    public String sha1(String input){
        if(input == null || input.equals("")){
            return "";
        }
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] bytes = digest.digest(input.getBytes());
//        return byteArrayToHexString(bytes).toString();
        return "";
    }
    public String evalParameter(String parameters) {
        if(StringUtils.isBlank(parameters)){
            return "";
        }else{
            Pattern pattern = Pattern.compile("\\$\\{.+?\\}");
            Matcher matcher = pattern.matcher(parameters);
            while (matcher.find()) {
                String group = matcher.group(0);
                String name = group.substring(2, group.length() - 1);
                String mubiao=null;
                if(!name.equals("sign")) {
                    if (!name.startsWith("system"))
                        mubiao = parameterMapper.getvalue(name);
                    else {
                        if (name.equals("system(-10minmstimestamp)")) {
                            mubiao = (new Date().getTime() - 10 * 60 * 1000) + "";
                        }
                    }
                    if(mubiao!=null)
                        parameters = parameters.replace(group, mubiao.replace("\"","\\\""));
                }
            }
        }
        return parameters;
    }
    private String doexpectsql(CaseBean c) {
        String result="";
        if(c.getsqlid().length()==0||c.getexpect_sql().length()==0)
            return "无数据库校验<br>";
        Map duanyanMap = JSON.parseObject(c.getexpect_sql());

        for(Object x:duanyanMap.keySet())
        {
            try {
                String y=c.getsqlid();
                AddressBean a=addressMapper.getAddressbyid(y);
                ConnectionDB db = new ConnectionDB("com.mysql.jdbc.Driver","jdbc:mysql://"+a.geturl()+":"+a.getport()
                        ,a.getusername(),a.getpassword());
                System.out.println("sql语句"+x);
                if(!(db.select2((String)x)+"").equals(duanyanMap.get(x)+""))
                    return result+"数据库断言FAIL<br>";
            } catch (Exception e) {
                return result+"数据库断言FAIL<br>";
            }
        }
        return result+"数据库断言通过<br>";
    }
    private static String doexpect(CaseBean c) {
        String result="";
        Map duanyanMap = JSON.parseObject(c.getexpect());
        try {
        for (Object x : duanyanMap.keySet()) {
            String y = String.valueOf(duanyanMap.get(x));
            result = result + "执行断言" + x+y+",";
            if (y.startsWith("(==)")) {
                String u=y.substring("(==)".length());
                if (u.equals(c.getResponseResult()))
                    return result + "断言成功(==)，其余断言自动不执行" + "<br>";
                else
                    return result + "返回断言FAIL<br>";
            }
            Object d = getValueByJPath1(JSON.parseObject(c.getResponseResult()), String.valueOf(x));
            if (y.startsWith("(int)")) {
                if ((int) d == parseInt(y.substring("(int)".length())))
                    result = result + "断言成功" + "<br>";
                else
                    return result + "返回断言FAIL<br>";
            } else if (y.startsWith("(int>)")) {
                if ((int) d > parseInt(y.substring("(int>)".length())))
                    result = result + "断言成功" + "<br>";
                else
                    return result + "返回断言FAIL<br>";
            }
            else if (y.startsWith("(Stringinlist)")) {
                String h=y.substring("(Stringinlist)".length());
                List f=new ArrayList();
                Collections.addAll(f, h.split(","));
                if (f.contains(d))
                    result = result + "断言成功" + "<br>";
                else
                    return result + "返回断言FAIL<br>";
            }
//                    <option value="(int<)">int小于</option>
            else if (y.startsWith("(int<)")) {
                if ((int) d < parseInt(y.substring("(int<)".length())))
                    result = result + "断言成功" + "<br>";
                else
                    return result + "返回断言FAIL<br>";
            }
//                    <option value="(int>=)">int大于等于</option>
            else if (y.startsWith("(int>=)")) {
                if ((int) d >= parseInt(y.substring("(int>=)".length())))
                    result = result + "断言成功" + "<br>";
                else
                    return result + "返回断言FAIL<br>";
            }
//                    <option value="(int<=)">int小于等于</option>
            else if (y.startsWith("(int<=)")) {
                if ((int) d <= parseInt(y.substring("(int<=)".length())))
                    result = result + "断言成功" + "<br>";
                else
                    return result + "返回断言FAIL<br>";
            }
//                    <option value="(int!=)">int!=</option>
            else if (y.startsWith("(int!=)")) {
                if ((int) d != parseInt(y.substring("(int!=)".length())))
                    result = result + "断言成功" + "<br>";
                else
                    return result + "返回断言FAIL<br>";
            }
            else if (y.startsWith("(boolean)")) {
                if ((boolean) d == (y.substring("(boolean)".length()).equals("true")))
                    result = result + "断言成功" + "<br>";
                else
                    return result + "返回断言FAIL<br>";
            }
            else if (y.startsWith("(listlength)")) {
                String h=y.substring("(listlength)".length());
                JSONArray g = JSON.parseArray(d.toString());
                if (Integer.parseInt(h)==g.size())
                    result = result + "断言成功" + "<br>";
                else
                    return result + "返回断言FAIL<br>";
            }
            else if (y.startsWith("(isnull)")) {
                if (((String) x).contains("[")) {
                    JSONArray e = (JSONArray) d;
                    if (e.size() == 0)
                        result = result + "断言成功" + "<br>";
                    else
                        return result + "返回断言FAIL<br>";
                } else {
                    if (d instanceof JSONArray) {
                        if (((JSONArray) d).size() != 0)
                            return result + "返回断言FAIL<br>";
                    } else if (d.equals(""))
                        result = result + "断言成功" + "<br>";
                    else
                        return result + "返回断言FAIL<br>";
                }
            }
//                    <option value="(isnotnull)">isnotnull</option>
            else if (y.startsWith("(isnotnull)")) {
                if (d instanceof String) {
                    if (!d.equals(""))
                        result = result + "断言成功" + "<br>";
                    else
                        return result + "返回断言FAIL<br>";
                }
            } else if (y.startsWith("(float)")) {
                if ((float) d == Float.parseFloat(y.substring("(float)".length())))
                    result = result + "断言成功" + "<br>";
                else
                    return result + "返回断言FAIL<br>";
            }
//                    <option value="(floatnotequal)">float!=</option>
            else if (y.startsWith("(floatnotequal)")) {
                if ((float) d != Float.parseFloat(y.substring("(floatnotequal)".length())))
                    result = result + "断言成功" + "<br>";
                else
                    return result + "返回断言FAIL<br>";
            }
//                    <option value="(Stringequal)">Stringequal</option>
            else if (y.startsWith("(Stringequal)")) {
                if ((d.toString()).equals(y.substring("(Stringequal)".length())))
                    result = result + "断言成功" + "<br>";
                else
                    return result + "返回断言FAIL<br>";
            }
//                    <option value="(Stringendwith)">Stringendwith</option>
            else if (y.startsWith("(Stringendwith)")) {
                if ((d.toString()).endsWith(y.substring("(Stringendwith)".length())))
                    result = result + "断言成功" + "<br>";
                else
                    return result + "返回断言FAIL<br>";
            }
//                    <option value="(Stringstartwith)">Stringstartwith</option>
            else if (y.startsWith("(Stringstartwith)")) {
                if ((d.toString()).startsWith(y.substring("(Stringstartwith)".length())))
                    result = result + "断言成功" + "<br>";
                else
                    return result + "返回断言FAIL<br>";
            }
//                    <option value="(Stringcontains)">Stringcontains</option>
            else if (y.startsWith("(Stringcontains)")) {
                String z = y.substring("(Stringcontains)".length());
                String v = d.toString();
                if (v.contains(z))
                    result = result + "断言成功" + "<br>";
                else
                    return result + "返回断言FAIL<br>";
            }
//                    <option value="(Stringnotequal)">Stringnotequal</option>
            else if (y.startsWith("(Stringnotequal)")) {
                if (!(d.toString()).equals(y.substring("(Stringnotequal)".length())))
                    result = result + "断言成功" + "<br>";
                else
                    return result + "返回断言FAIL<br>";
            }
//                    <option value="(Stringnotendwith)">Stringnotendwith</option>
            else if (y.startsWith("(Stringnotendwith)")) {
                if (!(d.toString()).endsWith(y.substring("(Stringnotendwith)".length())))
                    result = result + "断言成功" + "<br>";
                else
                    return result + "返回断言FAIL<br>";
            }
//                    <option value="(Stringnotstartwith)">Stringnotstartwith</option>
            else if (y.startsWith("(Stringnotstartwith)")) {
                if (!(d.toString()).startsWith(y.substring("(Stringnotstartwith)".length())))
                    result = result + "断言成功" + "<br>";
                else
                    return result + "返回断言FAIL<br>";
            }
//                    <option value="(Stringnotcontains)">Stringnotcontains</option>
            else if (y.startsWith("(Stringnotcontains)")) {
                if (!(d.toString()).contains(y.substring("(Stringnotcontains)".length())))
                    result = result + "断言成功" + "<br>";
                else
                    return result + "返回断言FAIL<br>";
            }
        }
        return result + "返回断言通过<br>";
    }catch (Exception e) {
        return result+"返回断言FAIL<br>";
    }
    }
    public static Object getValueByJPath1(JSONObject responseJson, String jpath){
        Object obj = responseJson;
        for(String s : jpath.split("\\.")) {
            if(!s.isEmpty()) {
                if(!(s.contains("[") || s.contains("]"))) {
                    obj = ((JSONObject) obj).get(s);
                }else if(s.contains("[") || s.contains("]")) {
                    int n=parseInt(s.split("\\[")[1].replaceAll("]", ""));
                    JSONArray obj2 =((JSONArray)((JSONObject)obj).get(s.split("\\[")[0]));
                    if(n>=0)
                        obj=obj2.get(n);
                    else
                        obj=obj2.get(obj2.size()+n);
                }
            }
        }
        return obj;
    }
    public void dopostcondition(PreconditionBean p,String result,String projectId) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        System.out.println("执行代码,参数一为"+p.getvalue1());
        String p1=p.getvalue1();
        String class1="";
        String method1="";
        if(p1.lastIndexOf(".")==-1) {
            class1="org.jeecg.modules.qatool.service.impl.A12Mock";
            method1=p.getvalue1();
        }
        else
        {
            class1="org.jeecg.modules.qatool.service.impl"+p1.substring(0,p1.lastIndexOf("."));
            method1=p1.substring(p1.lastIndexOf(".")+1);
        }
        Class clz = Class.forName(class1);
        Method method = clz.getMethod(method1, String.class);
        Constructor constructor = clz.getConstructor();
        Object object = constructor.newInstance();
        String StringVal=method.invoke(object, "caseresponseresult="+result+"&canshu="+p.getvalue2()).toString();
        if(p.getvalue3().length()!=0) {
            ParameterBean p2=new ParameterBean();
            p2.setvalue(StringVal);
            p2.setname(p.getvalue3());
            //更新数据
            if(parameterMapper.getParameterListbyname(p.getvalue3()).size()>0)
                //更新数据
                parameterMapper.updatevaluebyname(p2);
            else{
                p2.setdescription("系统自动创建"+p.getvalue3());
                parameterMapper.insertParameter(p2);
            }
        }
    }
    public String dopostProcessing(CaseBean caseBean){
        String result=caseBean.getResponseResult();
        String postProcessing=caseBean.getpostcondition();
        try{
            if(!StringUtils.isBlank(caseBean.getpostcondition())) {
                Map duanyanMap = JSON.parseObject(postProcessing);

                for(Object c:duanyanMap.keySet()){
                    if(c.toString().startsWith("(1)"))
                    {
                        String id=c.toString().substring("(1)".length(),c.toString().length());
                        PreconditionBean p=preconditionMapper.getPrecondition(id);
                        if(p.getvalue().equals("true")&&p.gettype().equals("code"))
                            dopostcondition(p,result,caseBean.getproject_id());
                        else
                            dopreProcessing(p,caseBean.getproject_id());
                    }
                    else{
                        Object d = getValueByJPath1(JSON.parseObject(caseBean.getResponseResult()), String.valueOf(duanyanMap.get(c)));
                        String mubiao= c.toString().substring("(2)".length(),c.toString().length());
                        ParameterBean p=new ParameterBean();
                        p.setvalue(String.valueOf(d));
                        p.setname(mubiao);
                        if(parameterMapper.getParameterListbyname(mubiao).size()>0)
                            //更新数据
                            parameterMapper.updatevaluebyname(p);
                        else{
                            p.setdescription("系统自动创建"+mubiao);
                            parameterMapper.insertParameter(p);
                        }
//                        parameterMapper.updatevaluebyname(p);
                    }
                }
                //先参数替换
            }
            return "后置处理通过<br>";
        }catch (Exception e) {
            return "后置处理失败FAIL";
        }
    }
}