package com.yahelei.dao.mysql.autotest;

import org.apache.ibatis.annotations.Param;
import com.yahelei.domain.autotest.PreconditionBean;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PreconditionMapper{

	
	List<PreconditionBean> getPreconditionList(@Param("type") String type,
											   @Param("description") String description);
	PreconditionBean getPrecondition(@Param("id") String id);

	void updatePrecondition(PreconditionBean preconditionBean);
	void deletePrecondition(@Param("id")int id);

	void insertPrecondition(PreconditionBean preconditionBean);
}
