package com.yahelei.exception;

import org.apache.commons.lang3.StringUtils;

public enum  ErrorCodeEnum {

    // 成功
    SUCCESS(200, "操作成功"),

    // 系统错误
    SYS_SERVER_INTERVAL_ERROR(100000, "服务器内部错误"),
    SYS_FAILED(100001, "操作失败"),
    SYS_INVALID_PARAMS(100002, "参数不合法"),
    SYS_NOT_FOUND(100003, "数据不存在"),
    SYS_METHOD_NOT_ALLOWED(100004, "请求方法不正确"),
    SYS_PATH_NOT_FOUND(100005, "请求路径不存在"),
    SYS_BAD_REQUEST(100006, "请求语法错误"),
    SYS_INVALID_LOGIN(100010, "登录状态失效"),
    LOGIN_FAILED(-1, "用户名或密码错误");



    public static ErrorCodeEnum parseInt(int value) {
        for (ErrorCodeEnum enumItem : ErrorCodeEnum.values()) {
            if (enumItem.code() == value) {
                return enumItem;
            }
        }
        return null;
    }

    private final int code;
    private final String msg;
    private String replacedMsg;


    ErrorCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    ErrorCodeEnum(int code, String msg, String replacedMsg) {
        this.code = code;
        this.replacedMsg = replacedMsg;
        this.msg = msg + replacedMsg;
    }

    public int code() {
        return this.code;
    }

    public String msg() {
        if (StringUtils.isNotBlank(this.replacedMsg)) {
            return this.msg + this.replacedMsg;
        }
        return this.msg;
    }

    public String replacedMsg() {
        return this.replacedMsg;
    }

    public void reSetMsg(String replacedMsg) {
        this.replacedMsg = replacedMsg;
    }
}
