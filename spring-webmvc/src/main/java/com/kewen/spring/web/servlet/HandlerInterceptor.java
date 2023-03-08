package com.kewen.spring.web.servlet;

import com.kewen.spring.core.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @descrpition  拦截器
 * @author kewen
 * @since 2023-03-08
 */
public interface HandlerInterceptor {

    /**
     * 前置方法
     */
    default boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    /**
     * 执行完业务操作的后置方法
     * @throws Exception
     */
    default void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                            @Nullable ModelAndView modelAndView) throws Exception {
    }

    /**
     * 不管是否失败都会执行的完成方法
     */
    default void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                 @Nullable Exception ex) throws Exception {
    }

}
