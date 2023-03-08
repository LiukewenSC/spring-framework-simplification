package com.kewen.spring.web.servlet;

import com.kewen.spring.core.lang.Nullable;

import javax.servlet.http.HttpServletRequest;

/**
 * @author kewen
 * @descrpition
 * @since 2023-03-06
 */
public interface HandlerMapping {
    @Nullable
    HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception;
}
