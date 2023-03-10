package com.kewen.spring.web.method.support;

import com.kewen.spring.core.MethodParameter;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.web.context.request.NativeWebRequest;

/**
 * @descrpition 返回值解析器
 * @author kewen
 * @since 2023-03-08
 */
public interface HandlerMethodReturnValueHandler {

    boolean supportsReturnType(MethodParameter parameter);


    void handleReturnValue(@Nullable Object returnValue, MethodParameter returnType,
                           ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception;
}
