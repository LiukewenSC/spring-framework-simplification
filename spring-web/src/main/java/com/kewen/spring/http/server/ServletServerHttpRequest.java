package com.kewen.spring.http.server;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;

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

    @Override
    public InputStream getBody() throws IOException {
        return servletRequest.getInputStream();
    }
}
