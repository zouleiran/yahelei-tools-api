package com.yahelei.controller.autotest;

import com.yahelei.dao.mysql.autotest.AddressMapper;
import com.yahelei.domain.autotest.AddressBean;
import com.yahelei.resentity.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/yahelei/address")
public class addressController {
    @Resource
    public AddressMapper addressMapper;
    private static final Logger logger = LoggerFactory.getLogger(addressController.class);
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Result<?> list(@RequestParam(value="description",required =false) String description,
                          @RequestParam(value="url",required =false) String url,
                          @RequestParam(value="type",required =false) String type
    )
    {
        logger.info("/address/list GET 方法被调用!!");
        List<AddressBean> list =addressMapper.getAddressList(url,description,type);
        for(AddressBean Address:list){
            if(Address.gettype().equals("sql"))
                Address.setpassword("******");
        }
        Result<List<AddressBean>> result = new Result<>();

        result.setResult(list);
        result.setSuccess(true);
        return result;
    }
    @RequestMapping(value = "/deleteaddress", method = RequestMethod.GET)
    @ResponseBody
    public Result<?> deleteaddress(@RequestParam(value="addressId", defaultValue = "-1") Integer addressId) {
        logger.info("/address/deleteaddress GET 方法被调用!!");
        Result<String> result = new Result<>();
        addressMapper.deleteaddress(addressId);
        result.setResult("查询成功");
        result.setSuccess(true);
        return result;
    }
    @RequestMapping(value = "/addoretidaddress", method = RequestMethod.GET)
    @ResponseBody
    public Result<?> addoretidaddress(
            @RequestParam(value="addressId", defaultValue = "-1") Integer addressId,
            @RequestParam(value="type") String type,
            @RequestParam(value="url") String url,
            @RequestParam(value="port") String port,
            @RequestParam(value="username") String username,
            @RequestParam(value="password") String password,
            @RequestParam(value="description") String description) {
        logger.info("/address/addoretidaddress GET 方法被调用!!");
        AddressBean addressBean=new AddressBean();
        addressBean.setid(addressId);
        addressBean.seturl(url);
        addressBean.settype(type);
        addressBean.setport(port);
        addressBean.setusername(username);
        if(password.equals("******"))
            addressBean.setpassword("-1");
        else
            addressBean.setpassword(password);
        addressBean.setdescription(description);
        if(addressId!=-1) {
            {
                addressBean.setid(addressId);
                addressMapper.updateaddress(addressBean);
            }
        }
        else
            addressMapper.insertaddress(addressBean);
        Result<String> result = new Result<>();

        result.setResult("操作成功");
        result.setSuccess(true);
        return result;
    }
}