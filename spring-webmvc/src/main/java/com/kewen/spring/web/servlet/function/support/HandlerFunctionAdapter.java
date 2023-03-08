package com.kewen.spring.web.servlet.function.support;

import com.kewen.spring.web.servlet.HandlerAdapter;
import com.kewen.spring.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author kewen
 * @descrpition
 * @since 2023-03-06
 */
public class HandlerFunctionAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return false;
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return null;
    }
}
