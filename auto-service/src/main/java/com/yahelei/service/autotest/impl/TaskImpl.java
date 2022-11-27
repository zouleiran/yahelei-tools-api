package com.yahelei.service.autotest.impl;

import com.yahelei.dao.mysql.autotest.*;
import com.yahelei.domain.autotest.*;
import com.yahelei.service.autotest.CaseService;
import com.yahelei.service.autotest.TaskService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.io.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.yahelei.service.autotest.impl.MailUtil.sendmail;

@Service
public class TaskImpl implements TaskService {
    @Resource
    public TaskMapper taskMapper;
    @Resource
    public CaseService caseService;
    @Resource
    public CaseMapper caseMapper;
    @Resource
    public LabelMapper labelMapper;
    @Resource
    public CaseExecuteLogMapper caseExecuteLogMapper;
    @Override
    public List<TaskBean> gettaskList(String type, String description) {
        List<TaskBean> a=taskMapper.gettaskList(type,description);
        for(TaskBean taskBean:a)
        {
            List<A> b =new ArrayList();
            switch (taskBean.gettype()){
                case "1":{
                    b=taskMapper.getListProject(taskBean.getprojectId());
                    break;
                }
                case "2":{
                    b=taskMapper.getListInterface(taskBean.getprojectId());
                    break;
                }
                case "3":{
                    b=taskMapper.getlabel(taskBean.getprojectId());
                    break;
                }
            }
            taskBean.setx(b);
        }
        return a;
    }

    @Override
    public List<A> getList(String type,String project_id) {
        List<A> b =new ArrayList();
        switch (type){
            case "1":{
                b=taskMapper.getListProject(project_id);
                break;
            }
            case "2":{
                b=taskMapper.getListInterface(project_id);
                break;
            }
            case "3":{
                b=taskMapper.getlabel(project_id);
                break;
            }
        }
        return b;
    }

    @Override
    public boolean executetask(Integer taskId,String host) {
        boolean flag=true;
        TaskBean t=taskMapper.gettaskbyid(taskId+"");
        long SerialId=System.currentTimeMillis()/1000;
        try {
            Set<Integer> caselist=new HashSet();
            String[] x=t.getvalue().split(",");
            switch (t.gettype())
            {
                case "1":
                {
                    caselist=caseMapper.getcaselist(x,"project_id");
                    break;
                }
                case "2":
                {
                    caselist=caseMapper.getcaselist(x,"Interface_id");
                    break;
                }
                case "3":
                {
                    caselist=caseMapper.getcaselist(x,"id");
                    break;
                }
                case "4":
                {
                    for(int i=0;i<x.length;i++) {
                        String caselist1=labelMapper.getdetailbyid(x[i]).getcaselist();
                        if(caselist1.length()>0)
                        {
                            String[] s=caselist1.split(",");
                            for(int j=0;j<s.length;j++)
                                caselist.add(Integer.valueOf(s[j]));
                        }
                    }
                    break;
                }
            }
            for(int caseid:caselist)
            {
                if(!caseService.execute2(caseid+"",taskId,"-1",SerialId,host,new HashMap()))
                    flag=false;
            }
        }
        catch (Exception e) {
            return false;
        }
        return true;
    }
    @Override
    public void sendreportemail(String sendlist, long serialId) throws MessagingException {
        List<Integer> tasklist = caseExecuteLogMapper.gettaskid(serialId);
        List<CaseExecuteLogBean> a;
        if(tasklist.get(0)!=-1) {
            a = caseExecuteLogMapper.getdetail(serialId);
        }
        else
            a=caseExecuteLogMapper.getdetailjenkins(serialId);
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
        String text=setemailtext(a);
        setemailattach(a);
        String reportpath = "/abc";
        String reportfilename = "自动化测试报告";
        sendmail(sendlist,text,a.get(0).gettaskdescription()+" 接口自动化测试报告",reportpath,reportfilename);
    }
    public String setemailtext(List<CaseExecuteLogBean> a) {
        int sum=0;
        int success=0;
        for(CaseExecuteLogBean caseExecuteLogBean:a){
            sum++;
            if(caseExecuteLogBean.getresult())
                success++;
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        String result1 = numberFormat.format((float) success / (float) sum * 100);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String reporturl=
                "http://127.0.0.1:8080/qatool/httpautotest/exectedLogManager?serialId="+a.get(0).getSerialId();
        String result="<div style=\"font-weight:normal\"><font size=\"4\" class=\"\">各位好，</font></div>\n" +
                "<div style=\"font-weight:normal\"><font size=\"4\" class=\"\"><br class=\"\">\n" +
                "&nbsp; &nbsp; &nbsp; "+sd.format(a.get(0).getCreateTime())+"执行的 "+a.get(0).gettaskdescription()+" 接口测试任务报告如下：<br class=\"\">\n" +
                "&nbsp; &nbsp; &nbsp;本次执行测试用例条数："+a.size()+"条；</font></div>\n" +
                "</div>\n" +
                "</div>\n" +
                "<div class=\"\">\n" +
                "<div dir=\"auto\" class=\"\" style=\"font-family: Helvetica, serif, EmojiFont; font-style: normal; letter-spacing: normal; text-align: start; text-indent: 0px; text-transform: none; white-space: normal; word-spacing: 0px; text-decoration: none; color: rgb(0, 0, 0); overflow-wrap: break-word;\">\n" +
                "<div>\n" +
                "<ul class=\"x_x_MailOutline\" style=\"font-weight:normal\">\n" +
                "<li class=\"\"><span class=\"\" style=\"font-size:large\">执行失败的条数 "+(sum-success)+"条</span></li><li class=\"\"><font size=\"4\" class=\"\">执行成功的条数 "+success+"条</font></li><li class=\"\"><font size=\"4\" class=\"\">执行成功率为 "+result1+"%</font></li></ul>\n" +
                "<div style=\"font-weight:normal\"><font size=\"4\" class=\"\"><br class=\"\">\n" +
                "</font></div>\n" +
                "<div style=\"font-weight:normal\"><font size=\"4\" class=\"\">测试用例执行详情请查看附件或者点击<a href=\""+reporturl+"\" target=\"_blank\" rel=\"noopener noreferrer\" class=\"\">报告链接</a></font></div>\n" +
                "<div><font size=\"4\" class=\"\"><br class=\"\">\n" +
                "</font></div>\n";
        return result;
    }
    public void setemailattach(List<CaseExecuteLogBean> a) {
        int sum=0;
        int success=0;
        for(CaseExecuteLogBean caseExecuteLogBean:a){
            sum++;
            if(caseExecuteLogBean.getresult())
                success++;
        }
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        String result1 = numberFormat.format((float) success / (float) sum * 100);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String title="";
        if(sum==success)
            title="接口自动化测试运行完成";
        else
            title="接口自动化测试运行失败";
        String c1="<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">  \n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\">  \n" +
                "<head>  \n" +
                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=gb2312\" />  \n" +
                "<title>"+a.get(0).gettaskdescription()+"接口自动化测试报告"+"</title>  \n" +
                "</head>  \n" +
                "<body>  \n" +
                "<div>" +
                "本次执行测试用例条数："+a.size()+"条；其中，执行失败的条数 "+(sum-success)+"条，执行成功的条数 "+success+"条。执行成功率为 "+result1+"%;\n" +
                "点击<a href=\"127.0.0.1:8080/qatool/httpautotest/exectedLogManager?serialId="+a.get(0).getSerialId()+"\">报告</a>跳转详情，概况如下</div>" +
                "<span id=\"spanFirstt\">第一页</span> <span id=\"spanPret\">上一页</span> <span id=\"spanNextt\">下一页</span> <span id=\"spanLastt\">最后一页</span> 第<span id=\"spanPageNumt\"></span>页/共<span id=\"spanTotalPaget\"></span>页" +
                "<table width=\"500\" border=\"1\" cellspacing=\"1\">  "+
                "<tr>  \n" +
                "    <th>执行时间</th>\n" +
                "    <th>测试接口用例描述</th>\n" +
                "    <th>接口路径</th>\n" +
                "    <th>执行日志</th>\n" +
                "    <th>是否成功</th>\n" +
                "</tr>  \n" +
                "<tbody id=\"tablelsw\">" ;
        for(CaseExecuteLogBean caseExecuteLogBean:a){
            c1=c1+"<tr>";
            c1=c1+"<td bgcolor=\"#FFFFFF\">"+sd.format(caseExecuteLogBean.getupdate_time())+"</td>";
            c1=c1+"<td bgcolor=\"#FFFFFF\">"+caseExecuteLogBean.getcasedescription()+"</td>";
            c1=c1+"<td bgcolor=\"#FFFFFF\">"+caseExecuteLogBean.getPath()+"</td>";
            c1=c1+"<td bgcolor=\"#FFFFFF\">"+caseExecuteLogBean.getdescription()+"</td>";
            c1=c1+"<td bgcolor=\"#FFFFFF\">"+caseExecuteLogBean.getresult()+"</td>";
            c1=c1+"</tr>";
        }

        c1=c1+ "  </tbody>  \n" +
                "</table>  \n" +
                "<span id=\"spanFirst\">第一页</span> \n" +
                "<span id=\"spanPre\">上一页</span> \n" +
                "<span id=\"spanNext\">下一页</span> \n" +
                "<span id=\"spanLast\">最后一页</span> \n" +
                "第<span id=\"spanPageNum\"></span>页/共<span id=\"spanTotalPage\"></span>页  "+
                "</body>  \n" +
                "</html>  \n"
                +"<script>   \n" +
                "var theTable = document.getElementById(\"tablelsw\");   \n" +
                "var totalPage = document.getElementById(\"spanTotalPage\");   \n" +
                "var pageNum = document.getElementById(\"spanPageNum\");   \n" +
                "  \n" +
                "var spanPre = document.getElementById(\"spanPre\");   \n" +
                "var spanNext = document.getElementById(\"spanNext\");   \n" +
                "var spanFirst = document.getElementById(\"spanFirst\");   \n" +
                "var spanLast = document.getElementById(\"spanLast\");   \n" +
                "  \n" +
                "var totalPaget = document.getElementById(\"spanTotalPaget\");   \n" +
                "var pageNumt = document.getElementById(\"spanPageNumt\");   \n" +
                "  \n" +
                "var spanPret = document.getElementById(\"spanPret\");   \n" +
                "var spanNextt = document.getElementById(\"spanNextt\");   \n" +
                "var spanFirstt = document.getElementById(\"spanFirstt\");   \n" +
                "var spanLastt = document.getElementById(\"spanLastt\");   \n" +
                "  \n" +
                "var numberRowsInTable = theTable.rows.length;   \n" +
                "var pageSize = 10;   \n" +
                "var page = 1;   \n" +
                "  \n" +
                "//下一页   \n" +
                "function next(){   \n" +
                "  \n" +
                "    hideTable();   \n" +
                "       \n" +
                "    currentRow = pageSize * page;   \n" +
                "    maxRow = currentRow + pageSize;   \n" +
                "    if ( maxRow > numberRowsInTable ) maxRow = numberRowsInTable;   \n" +
                "    for ( var i = currentRow; i< maxRow; i++ ){   \n" +
                "        theTable.rows[i].style.display = '';   \n" +
                "    }   \n" +
                "    page++;   \n" +
                "       \n" +
                "    if ( maxRow == numberRowsInTable ) { nextText(); lastText(); }   \n" +
                "    showPage();   \n" +
                "    preLink();   \n" +
                "    firstLink();   \n" +
                "}   \n" +
                "  \n" +
                "//上一页   \n" +
                "function pre(){   \n" +
                "  \n" +
                "    hideTable();   \n" +
                "       \n" +
                "    page--;   \n" +
                "       \n" +
                "    currentRow = pageSize * page;   \n" +
                "    maxRow = currentRow - pageSize;   \n" +
                "    if ( currentRow > numberRowsInTable ) currentRow = numberRowsInTable;   \n" +
                "    for ( var i = maxRow; i< currentRow; i++ ){   \n" +
                "        theTable.rows[i].style.display = '';   \n" +
                "    }   \n" +
                "       \n" +
                "       \n" +
                "    if ( maxRow == 0 ){ preText(); firstText(); }   \n" +
                "    showPage();   \n" +
                "    nextLink();   \n" +
                "    lastLink();   \n" +
                "}   \n" +
                "  \n" +
                "//第一页   \n" +
                "function first(){   \n" +
                "    hideTable();   \n" +
                "    page = 1;   \n" +
                "    for ( var i = 0; i<pageSize; i++ ){   \n" +
                "        theTable.rows[i].style.display = '';   \n" +
                "    }   \n" +
                "    showPage();   \n" +
                "       \n" +
                "    preText();   \n" +
                "    nextLink();   \n" +
                "    lastLink();   \n" +
                "}   \n" +
                "  \n" +
                "//最后一页   \n" +
                "function last(){   \n" +
                "    hideTable();   \n" +
                "    page = pageCount();   \n" +
                "    currentRow = pageSize * (page - 1);   \n" +
                "    for ( var i = currentRow; i<numberRowsInTable; i++ ){   \n" +
                "        theTable.rows[i].style.display = '';   \n" +
                "    }   \n" +
                "    showPage();   \n" +
                "       \n" +
                "    preLink();   \n" +
                "    nextText();   \n" +
                "    firstLink();   \n" +
                "}   \n" +
                "  \n" +
                "function hideTable(){   \n" +
                "    for ( var i = 0; i<numberRowsInTable; i++ ){   \n" +
                "        theTable.rows[i].style.display = 'none';   \n" +
                "    }   \n" +
                "}   \n" +
                "  \n" +
                "function showPage(){   \n" +
                "    pageNum.innerHTML = page;   \n" +
                "    pageNumt.innerHTML = page;   \n" +
                "}   \n" +
                "  \n" +
                "//总共页数   \n" +
                "function pageCount(){   \n" +
                "    var count = 0;   \n" +
                "    if ( numberRowsInTable%pageSize != 0 ) count = 1;    \n" +
                "    return parseInt(numberRowsInTable/pageSize) + count;   \n" +
                "}   \n" +
                "  \n" +
                "//显示链接   \n" +
                "function preLink(){ spanPre.innerHTML = \"<a href='javascript:pre();'>上一页</a>\"; spanPret.innerHTML = \"<a href='javascript:pre();'>上一页</a>\";}   \n" +
                "function preText(){ spanPre.innerHTML = \"上一页\"; spanPret.innerHTML = \"上一页\"; }   \n" +
                "  \n" +
                "function nextLink(){ spanNext.innerHTML = \"<a href='javascript:next();'>下一页</a>\"; spanNextt.innerHTML = \"<a href='javascript:next();'>下一页</a>\";}   \n" +
                "function nextText(){ spanNext.innerHTML = \"下一页\"; spanNextt.innerHTML = \"下一页\";}   \n" +
                "  \n" +
                "function firstLink(){ spanFirst.innerHTML = \"<a href='javascript:first();'>第一页</a>\"; spanFirstt.innerHTML = \"<a href='javascript:first();'>第一页</a>\";}   \n" +
                "function firstText(){ spanFirst.innerHTML = \"第一页\"; spanFirstt.innerHTML = \"第一页\";}   \n" +
                "  \n" +
                "function lastLink(){ spanLast.innerHTML = \"<a href='javascript:last();'>最后一页</a>\"; spanLastt.innerHTML = \"<a href='javascript:last();'>最后一页</a>\";}   \n" +
                "function lastText(){ spanLast.innerHTML = \"最后一页\"; spanLastt.innerHTML = \"最后一页\";}   \n" +
                "  \n" +
                "//隐藏表格   \n" +
                "function hide(){   \n" +
                "    for ( var i = pageSize; i<numberRowsInTable; i++ ){   \n" +
                "        theTable.rows[i].style.display = 'none';   \n" +
                "    }   \n" +
                "       \n" +
                "    totalPage.innerHTML = pageCount();   \n" +
                "    pageNum.innerHTML = '1';   \n" +
                "       \n" +
                "    totalPaget.innerHTML = pageCount();   \n" +
                "    pageNumt.innerHTML = '1';   \n" +
                "       \n" +
                "    nextLink();   \n" +
                "    lastLink();   \n" +
                "}   \n" +
                "  \n" +
                "hide();   \n" +
                "</script>  ";
        String reportpath = "abc";
        File d=new File(reportpath);
        FileOutputStream writerStream = null;
        try {
            writerStream = new FileOutputStream(reportpath);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(writerStream, "GBK"));
            writer.write(c1);
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
//    @Override
//    public String executebygit(String git, String jenkins,String labelname) {
//        Set<Integer> caselist=new HashSet();
//        List<LabelBean> k=labelMapper.getLabelListbyjenkins(git,labelname);
//        if(labelname.length()==0||k.size()==0)
//            caselist=caseMapper.getcasebygit(git);
//        else
//            caselist=caseMapper.getcasebylabel(k.get(0).getProjectId(),"%"+k.get(0).getid()+"%");
//        long SerialId=System.currentTimeMillis()/1000;
//        for(int caseid:caselist)
//        {
//            try {
//                caseService.executecaseid(caseid+"",-1,SerialId, jenkins,"");
//            } catch (Exception e) {
//                System.out.println("失败");
//            }
//        }
//        return "http://www.yahelei.com/qatool/httpautotest/exectedLogManager?serialId="+SerialId;
//    }
}