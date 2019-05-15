package com.springboot.common.interceptor;

import com.springboot.common.constant.CommonConstant;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class TimeOutInterceptor implements HandlerInterceptor {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeOutInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        String requestUrl = request.getRequestURI().replace(request.getContextPath(), "");
        response.setContentType("text/html;charset=utf-8");
        HttpSession session = request.getSession(true);
        if (StringUtils.isNoneBlank(requestUrl)) {
            for (String url : CommonConstant.NONE_PERMISSION_RES) {
                if (requestUrl.contains(url)) {
                    return true;
                }
            }
        }
        //session持续时间
        int maxInactiveInterval = session.getMaxInactiveInterval();
        //session创建时间
        long creationTime = session.getCreationTime();
        //session最新链接时间
        long lastAccessedTime = session.getLastAccessedTime();
        LOGGER.info("-----> maxInactiveInterval: " + maxInactiveInterval);
        LOGGER.info("-----> creationTime: " + creationTime);
        LOGGER.info("-----> lastAccessedTime: " + lastAccessedTime);
        //从session获取上次链接时间
        Long operateTime = (Long) session.getAttribute("operateTime");
        LOGGER.info("-----> operateTime: " + operateTime);
        //如果operateTime是空，说明是第一次链接，对operateTime进行初始化
        if (operateTime == null) {
            session.setAttribute("operateTime", lastAccessedTime);
            return true;
        } else {
            //计算最新链接时间和上次链接时间的差值
            int intervalTime = (int) ((lastAccessedTime - operateTime) / 1000);
            LOGGER.info("-----> intervalTime: " + intervalTime);
            //如果超过三十分钟没有交互的话，就跳转到超时界面
            if (intervalTime > 1800) {
                response.setHeader(CommonConstant.EXPOSED_HEADERS,"expired");
//                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }
            //更新operateTime
            session.setAttribute("operateTime", lastAccessedTime);
            return true;
        }
    }
}
