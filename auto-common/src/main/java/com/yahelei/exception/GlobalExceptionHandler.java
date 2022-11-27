package com.yahelei.exception;

import com.yahelei.resentity.BaseResMsg;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    public BaseResMsg customException(Exception e) {
        e.printStackTrace();

        return BaseResMsg.builder()
                .code(ErrorCodeEnum.SYS_SERVER_INTERVAL_ERROR.code())
                .msg("error")
                .data("系统发生异常！ msg: " + e.getMessage())
                .build();
    }
}
