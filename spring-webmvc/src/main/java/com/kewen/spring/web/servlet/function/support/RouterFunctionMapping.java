package com.kewen.spring.web.servlet.function.support;

import com.kewen.spring.web.servlet.HandlerMapping;
import com.kewen.spring.web.servlet.handler.AbstractHandlerMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author kewen
 * @descrpition
 * @since 2023-03-06
 */
public class RouterFunctionMapping extends AbstractHandlerMapping {
    @Override
    protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
        return null;
    }
}
