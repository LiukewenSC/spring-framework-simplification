package com.kewen.spring.http.server;

import javax.servlet.http.HttpServletRequest;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-09
 */
public class ServletServerHttpRequest implements ServerHttpRequest{

    HttpServletRequest servletRequest;

    public ServletServerHttpRequest(HttpServletRequest servletRequest) {
        this.servletRequest = servletRequest;
    }

    public HttpServletRequest getServletRequest() {
        return servletRequest;
    }
}
