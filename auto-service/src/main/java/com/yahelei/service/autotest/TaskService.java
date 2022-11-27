package com.yahelei.service.autotest;
import com.yahelei.domain.autotest.A;
import com.yahelei.domain.autotest.TaskBean;

import javax.mail.MessagingException;
import java.util.List;

public interface TaskService {

    List<TaskBean> gettaskList(String type,
                               String description);

    List<A> getList(String type,String project_id);

    boolean executetask(Integer taskId,String host);

    void sendreportemail(String sendlist, long serialId) throws MessagingException;
}