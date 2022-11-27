package com.yahelei.dao.mysql.autotest;

import org.apache.ibatis.annotations.Param;
import com.yahelei.domain.autotest.InterfaceBean;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface InterfaceMapper{


	List<InterfaceBean> getInterfacelist(@Param("url") String url,
										 @Param("project_id") String project_id,
										 @Param("description") String description);
	List<InterfaceBean> getInterfacelistequal(@Param("url") String url,
										  @Param("project_id") String project_id);
	void deleteInterface(@Param("interfaceId") Integer interfaceId);

	void updateInterface(InterfaceBean interfaceBean);

	void insertInterface(InterfaceBean interfaceBean);

    InterfaceBean getdetailbyInterfaceid(@Param("interfaceId") String interfaceid);
}
