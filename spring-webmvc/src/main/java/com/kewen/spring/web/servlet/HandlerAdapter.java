package com.kewen.spring.web.servlet;

import com.kewen.spring.core.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author kewen
 * @descrpition 处理器适配器
 * @since 2023-03-06
 */
public interface HandlerAdapter {
    /**
     * 判定是否支持
     * @param handler
     * @return
     */
    boolean supports(Object handler);

    /**
     * 执行Handler方法
     */
    @Nullable
    ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception;
}
