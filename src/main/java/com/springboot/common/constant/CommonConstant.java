package com.springboot.common.constant;

public interface CommonConstant {
    String REDIS_HOST = "127.0.0.1";

    Integer REDIS_PORT = 6379;

    String REDIS_PASSWORD = "123456";

    String EXPOSED_HEADERS = "Authorization";
    /**
     * 不需要权限验证的资源表达式
     */
    String[] NONE_PERMISSION_RES = {"/captcha/init", "/captcha/draw", "/captcha/sms", "/login", "/logout"};
}
