package com.kewen.spring.web.servlet;

import com.kewen.spring.core.lang.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @descrpition 控制器链
 * @author kewen
 * @since 2023-03-08
 */
public class HandlerExecutionChain {

    /**
     * 一般为HandlerMethod
     */
    Object handler;

    @Nullable
    private List<HandlerInterceptor> interceptorList;

    private int interceptorIndex = -1;

    public HandlerExecutionChain(Object handler) {
        this.handler=handler;
        this.interceptorList = new ArrayList<>();
    }

    public void addInterceptor(HandlerInterceptor interceptor) {
        interceptorList.add(interceptor);
    }
}
