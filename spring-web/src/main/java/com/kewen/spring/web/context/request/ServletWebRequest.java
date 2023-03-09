package com.kewen.spring.web.context.request;

import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-08
 */
public class ServletWebRequest implements NativeWebRequest{
    HttpServletRequest request;
    HttpServletResponse response;

    public ServletWebRequest(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
    public final HttpServletRequest getRequest() {
        return this.request;
    }

    @Override
    public <T> T getNativeRequest(@Nullable Class<T> requiredType) {
        return WebUtils.getNativeRequest(getRequest(), requiredType);
    }

    @Override
    public String[] getParameterValues(String name) {
        return getRequest().getParameterValues(name);
    }
}
