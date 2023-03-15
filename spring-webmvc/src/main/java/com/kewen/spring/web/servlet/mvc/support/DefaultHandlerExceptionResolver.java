package com.kewen.spring.web.servlet.mvc.support;

import com.kewen.spring.web.servlet.HandlerExceptionResolver;
import com.kewen.spring.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author kewen
 * @descrpition
 * @since 2023-03-06
 */
public class DefaultHandlerExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        return null;
    }
}
