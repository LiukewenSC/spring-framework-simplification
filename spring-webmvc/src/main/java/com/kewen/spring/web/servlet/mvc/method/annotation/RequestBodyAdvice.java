package com.kewen.spring.web.servlet.mvc.method.annotation;

import com.kewen.spring.core.MethodParameter;
import com.kewen.spring.http.converter.HttpMessageConverter;
import com.kewen.spring.http.server.ServletServerHttpRequest;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-10
 */
public interface RequestBodyAdvice<T> {
    /**
     * 是否支持读取数据
     * @param methodParameter
     * @param targetType
     * @param converterType
     * @return
     */
    boolean supports(MethodParameter methodParameter, Type targetType,
                     Class<? extends HttpMessageConverter<?>> converterType);

    /**
     * 读取数据前的操作
     * @param request
     * @param parameter
     * @param targetType
     * @param converterType
     * @return
     * @throws IOException
     */
    default ServletServerHttpRequest beforeBodyRead(ServletServerHttpRequest request, MethodParameter parameter,
                                                   Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return request;
    }


    /**
     * 读取数据后的操作
     * @param body
     * @param inputMessage
     * @param parameter
     * @param targetType
     * @param converterType
     * @return
     */
    default Object afterBodyRead(Object body, ServletServerHttpRequest inputMessage, MethodParameter parameter,
                         Type targetType, Class<? extends HttpMessageConverter<?>> converterType){
        return body;
    }
}
