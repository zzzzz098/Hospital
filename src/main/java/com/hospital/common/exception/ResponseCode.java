package com.hospital.common.exception;

public enum ResponseCode {
    SUCCESS(200, "success"),
    UNAUTHORIZED(401, "未登录"),
    FORBIDDEN(403, "无权限"),
    LOGIN_ERROR(1001, "用户名或密码错误"),
    PARAM_ERROR(400, "参数错误"),
    NOT_FOUND(404, "资源不存在");

    private final int code;
    private final String message;

    ResponseCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
