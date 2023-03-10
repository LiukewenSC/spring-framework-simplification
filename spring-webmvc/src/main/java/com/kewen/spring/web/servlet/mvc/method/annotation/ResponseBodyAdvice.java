package com.kewen.spring.web.servlet.mvc.method.annotation;

import com.kewen.spring.core.MethodParameter;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.http.MediaType;
import com.kewen.spring.http.converter.HttpMessageConverter;
import com.kewen.spring.http.server.ServerHttpRequest;
import com.kewen.spring.http.server.ServerHttpResponse;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-10
 */
public interface ResponseBodyAdvice <T>{
    /**
     * 是否支持
     * @param returnType
     * @param converterType
     * @return
     */
    boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType);

    /**
     * 写数据前的最后操作
     * @param body
     * @param returnType
     * @param selectedContentType
     * @param selectedConverterType
     * @param request
     * @param response
     * @return
     */
    @Nullable
    T beforeBodyWrite(@Nullable T body, MethodParameter returnType, MediaType selectedContentType,
                      Class<? extends HttpMessageConverter<?>> selectedConverterType,
                      ServerHttpRequest request, ServerHttpResponse response);

}
