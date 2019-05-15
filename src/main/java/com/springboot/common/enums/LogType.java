package com.springboot.common.enums;

public enum LogType {
    /**
     * 登录日志
     */
    LOGIN("登录日志"),
    /**
     * 业务日志
     */
    BUSINESS("业务日志");

    String message;

    LogType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
