package com.yahelei.dao.mysql.autotest;

import org.apache.ibatis.annotations.Param;
import com.yahelei.domain.autotest.AddressBean;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AddressMapper {

    List<AddressBean> getAddressList(@Param("url") String url,
                                     @Param("description") String description,
                                     @Param("type") String type);

    void deleteaddress(@Param("id") Integer id);

    void updateaddress(AddressBean addressBean);

    void insertaddress(AddressBean addressBean);

    AddressBean getAddressbyid(@Param("value") String getvalue);

    AddressBean getDatabaseAddressbyid(@Param("value") String getvalue);
}
