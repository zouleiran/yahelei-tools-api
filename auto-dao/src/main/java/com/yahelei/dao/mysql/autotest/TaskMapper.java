package com.yahelei.dao.mysql.autotest;

import com.yahelei.domain.autotest.A;
import com.yahelei.domain.autotest.LabelBean;
import org.apache.ibatis.annotations.Param;
import com.yahelei.domain.autotest.TaskBean;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TaskMapper {

    void updatetask(TaskBean taskBean);

    List<TaskBean> gettaskList(@Param("type")String type,
                               @Param("description") String description);
    void deletetask(@Param("id")int id);

    void inserttask(TaskBean taskBean);

    TaskBean gettaskbyid(@Param("task_id")String task_id);

    List<A> getListProject(@Param("project_id")String project_id);

    List<A> getListInterface(@Param("project_id")String project_id);

    List<A> getListCase(@Param("project_id")String project_id);
    List<A> getlabel(@Param("project_id")String project_id);
}
