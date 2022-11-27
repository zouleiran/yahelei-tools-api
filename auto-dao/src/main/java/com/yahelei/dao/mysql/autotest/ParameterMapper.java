package com.yahelei.dao.mysql.autotest;

import org.apache.ibatis.annotations.Param;
import com.yahelei.domain.autotest.ParameterBean;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ParameterMapper {
	List<ParameterBean> getParameterList(@Param("name") String name,
										 @Param("description") String description
	);
	List<ParameterBean> getParameterListbyname(@Param("name") String name);
	String getvalue(@Param("name") String name);
	void updateParameter(ParameterBean parameterBean);
	void deleteParameter(@Param("parameterId")int parameterId);
	void insertParameter(ParameterBean parameterBean);
    void updatevaluebyname(ParameterBean p);
}
