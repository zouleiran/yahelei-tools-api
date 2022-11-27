package com.yahelei.dao.mysql.autotest;

import org.apache.ibatis.annotations.Param;
import com.yahelei.domain.autotest.CaseBean;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
@Repository
public interface CaseMapper {

	CaseBean getdetailbyid(@Param("case_id") String caseid);

	int getcasenum(@Param("Interface_id") int getid);

    List<CaseBean> getcaselistbyInterface_id(@Param("Interface_id")String Interface_id,@Param("project_id") String project_id);

	CaseBean getcaseaddinfo(@Param("project_id") String project_id, @Param("Interface_id") String Interface_id);

	void updatecase(CaseBean c);

	int insertcase(CaseBean c);

	List<Integer> getpreconditionnum(@Param("precondition1") String precondition1,@Param("precondition") String precondition
			,@Param("precondition2") String precondition2,@Param("precondition3") String precondition3,@Param("Postcondition4") String Postcondition4 );
    List<CaseBean> getcaselistbydescription(@Param("description") String description,
											@Param("Interface_id") String Interface_id,
											@Param("project_id") String project_id);

    Set<Integer> getcaselist(@Param("x") String[] x, @Param("canshu") String canshu);

    void deletecase(Integer id);

	Set<Integer> getcasebylabel(@Param("projectId") String projectId,
								 @Param("labelId") String labelId);

	Set<Integer> getcasebygit(@Param("git") String git);
}
