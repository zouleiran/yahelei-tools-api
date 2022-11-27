package com.yahelei.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yahelei.domain.respones.ListDto;
import com.yahelei.exception.ErrorCodeEnum;
import com.yahelei.resentity.BaseResMsg;

import java.util.List;

public class BaseController {

    protected void printJson (Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object));
    }

    /**
     * 成功响应
     *
     * @param data 数据
     * @return BaseResMsg
     */
    protected BaseResMsg success(Object data, ErrorCodeEnum errorCodeEnum) {
        return BaseResMsg.builder().code(errorCodeEnum.code()).msg(errorCodeEnum.msg()).data(data).build();
    }

    /**
     * 成功响应
     *
     * @param data 数据
     * @return BaseResMsg
     */
    protected BaseResMsg success(Object data) {
        return success(data, ErrorCodeEnum.SUCCESS);
    }

    /**
     * 成功响应
     *
     * @param data 数据
     * @return BaseResMsg
     */
    @SuppressWarnings("unchecked")
    protected BaseResMsg success(List data) {
        return success(ListDto.builder().list(data).build(), ErrorCodeEnum.SUCCESS);
    }

    /**
     * 成功响应
     *
     * @return BaseResMsg
     */
    protected BaseResMsg success() {
        return BaseResMsg.builder().code(200).msg("").build();
    }
}
