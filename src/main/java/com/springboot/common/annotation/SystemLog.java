package com.springboot.common.annotation;

import com.springboot.common.enums.LogType;

import java.lang.annotation.*;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface SystemLog {

    /**
     * 业务的名称,例如:"修改菜单"
     */
    String value() default "";

    /**
     * 日志类型
     * @return
     */
    LogType type() default LogType.BUSINESS;
}
