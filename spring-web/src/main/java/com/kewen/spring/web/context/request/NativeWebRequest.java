package com.kewen.spring.web.context.request;

import com.kewen.spring.core.lang.Nullable;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-08
 */
public interface NativeWebRequest extends WebRequest{
    @Nullable
    <T> T getNativeRequest(@Nullable Class<T> requiredType);

    String[] getParameterValues(String name);
}
