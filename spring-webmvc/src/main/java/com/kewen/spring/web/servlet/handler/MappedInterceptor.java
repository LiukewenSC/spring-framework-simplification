package com.kewen.spring.web.servlet.handler;

import com.kewen.spring.web.servlet.HandlerInterceptor;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-08
 */
public class MappedInterceptor implements HandlerInterceptor {
    private final HandlerInterceptor interceptor;

    public MappedInterceptor(HandlerInterceptor interceptor) {
        this.interceptor = interceptor;
    }

    public HandlerInterceptor getInterceptor() {
        return interceptor;
    }
}
