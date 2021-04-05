package com.codeleven.web.utils;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

public class RequestUtil {
    /**
     * 获取当前GET请求里面的参数
     * @param paramName 参数名称
     * @return String 参数对应的值
     */
    public static String getParam(String paramName) {
        String paramValue = getCurrentRequest().getParameter(paramName);
        if(paramValue != null){
            return paramValue;
        }
        return null;
    }

    /**
     * 获取当前的请求
     * @return HttpServletRequest Http请求
     */
    public static HttpServletRequest getCurrentRequest(){
        RequestAttributes requestAttr = RequestContextHolder.getRequestAttributes();
        if (null != requestAttr) {
            return ((ServletRequestAttributes)requestAttr).getRequest();
        }
        return null;
    }
}
