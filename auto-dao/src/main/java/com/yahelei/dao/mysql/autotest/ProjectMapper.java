package com.yahelei.dao.mysql.autotest;

import org.apache.ibatis.annotations.Param;
import com.yahelei.domain.autotest.ProjectBean;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProjectMapper {
	List<ProjectBean> getProjectList(@Param("host") String host,
                                     @Param("description") String description
    );
    List<ProjectBean> getProjectListequal(@Param("host") String host
    );
    ProjectBean getProjectByprojectId(@Param("projectId")Integer projectId);

    void updateproject(ProjectBean projectBean);

    void insertproject(ProjectBean projectBean);

    void deleteproject(@Param("projectId")Integer projectId);
}
