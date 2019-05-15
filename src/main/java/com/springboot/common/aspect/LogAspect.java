package com.springboot.common.aspect;

import com.google.gson.Gson;
import com.springboot.common.annotation.SystemLog;
import com.springboot.common.shiro.ShiroKit;
import com.springboot.model.Log;
import com.springboot.service.ILogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.NamedThreadLocal;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

@Aspect
@Component
public class LogAspect {
    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);
    private static final ThreadLocal<Date> beginTimeThreadLocal = new NamedThreadLocal<>("ThreadLocal beginTime");

    @Autowired
    private ILogService logService;

    @Pointcut("@annotation(com.springboot.common.annotation.SystemLog)")
    public void logPointcut() {
        LOGGER.info("========controllerAspect===========");
    }

    @Before("logPointcut()")
    public void doBefore(JoinPoint point) throws Throwable {
        Date beginTime = new Date();
        beginTimeThreadLocal.set(beginTime);
    }

    @After("logPointcut()")
    public void recordSysLog(JoinPoint point) throws Throwable {
        Log sysLog = new Log();

        //用时
        long beginTime = beginTimeThreadLocal.get().getTime();
        long time = System.currentTimeMillis() - beginTime;

        // 用户操作，注解上的描述
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        SystemLog systemLog = method.getAnnotation(SystemLog.class);

        //日志类型
        String type = systemLog.type().getMessage();

        // 请求的方法名
        String className = point.getTarget().getClass().getName();
        String methodName = signature.getName();

        // 获取参数
        Object[] params = point.getArgs();

        //获取IP
        HttpServletRequest request = null;
        if (params != null && params.length > 0) {
            request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        }

        String msg = String.format("[类名]:%s,[方法]:%s,[参数]:%s", className, methodName, new Gson().toJson(params));
        LOGGER.info(msg);

        sysLog.setUserId(ShiroKit.getUser() == null ? 000000L : ShiroKit.getUser().getId());//用户ID
        sysLog.setUsername(ShiroKit.getUser() == null ? "" : ShiroKit.getUser().getUsername());// 用户名
        sysLog.setLogType(type); // 日志类型
        sysLog.setOperation(systemLog.value());// 用户操作
        sysLog.setTime((int) time);//用时
        sysLog.setMethod(className + "." + methodName + "()");// 请求的方法名
//        sysLog.setParams(new Gson().toJson(params));// 请求的参数
        if (request != null) {
            sysLog.setIp(request.getRemoteAddr());// 设置IP地址
        }
        sysLog.setCreateTime(new Date());// 当前时间

        logService.insert(sysLog);// 保存系统日志
    }
}
