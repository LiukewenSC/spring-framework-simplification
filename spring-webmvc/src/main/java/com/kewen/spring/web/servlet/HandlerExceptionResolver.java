package com.kewen.spring.web.servlet;

import com.kewen.spring.core.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author kewen
 * @descrpition 异常处理器
 * @since 2023-03-06
 */
public interface HandlerExceptionResolver {
    ModelAndView resolveException(
            HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex);
}
