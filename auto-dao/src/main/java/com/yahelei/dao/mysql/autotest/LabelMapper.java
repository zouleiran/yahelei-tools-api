package com.yahelei.dao.mysql.autotest;

import com.yahelei.domain.autotest.LabelBean;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LabelMapper {

    List<LabelBean> getLabelList(@Param("projectId") String projectId,
                                 @Param("description") String description);

    void deleteLabel(@Param("id") Integer id);

    void updatelabel(LabelBean labelBean);

    void insertlabel(LabelBean labelBean);

    LabelBean getdetailbyid(@Param("id") String id);

    void reduceLabel(@Param("labelid") String id,@Param("caselist") String caselist);

    void addLabel(@Param("labelid")String id, @Param("caselist")String caselist);
    List<LabelBean> getLabelListbyjenkins(@Param("git") String git,
                                 @Param("description") String description);

    List<LabelBean> getLabellist2();

    List<LabelBean> getexeclabellist();
}
