package com.kewen.spring.web.context.request;

import com.kewen.spring.core.lang.Nullable;

import javax.servlet.http.HttpServletResponse;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-08
 */
public interface NativeWebRequest extends WebRequest{

    String[] getParameterValues(String name);


    @Nullable
    <T> T getNativeRequest(@Nullable Class<T> requiredType);

    @Nullable
    <T> T getNativeResponse(@Nullable Class<T> requiredType);
}
