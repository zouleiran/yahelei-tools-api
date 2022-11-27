package com.yahelei.dao.mysql.autotest;

import org.apache.ibatis.annotations.Param;
import com.yahelei.domain.autotest.CaseExecuteLogBean;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CaseExecuteLogMapper {

	void add(CaseExecuteLogBean caseExecutelogbean);

	List<CaseExecuteLogBean> getlistbycaseid(CaseExecuteLogBean caseExecutelogbean);
	List<CaseExecuteLogBean> getlist(@Param("serialId") long serialId,
									 @Param("taskId") int taskid,
									 @Param("pagecount") int pagecount,
									 @Param("begin") int begin);

	List<CaseExecuteLogBean> getdetail(@Param("serialId") long serialId);

	Integer getsuccess(@Param("serialId") long serialId);

	List<Integer> getsum(@Param("serialId") long serialId,@Param("taskId") int taskid);
	List<CaseExecuteLogBean> getdetailjenkins(@Param("serialId") long serialId);
	List<Integer> gettaskid(@Param("serialId") long serialId);
}
