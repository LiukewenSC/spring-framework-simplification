package com.kewen.spring.web.sample.advance;

import com.kewen.spring.context.annotation.Component;
import com.kewen.spring.core.MethodParameter;
import com.kewen.spring.http.converter.HttpMessageConverter;
import com.kewen.spring.http.server.ServletServerHttpRequest;
import com.kewen.spring.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * @author kewen
 * @descrpition
 * @since 2023-03-14
 */
@Component
public class RequestRequestBodyAdvice implements RequestBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public ServletServerHttpRequest beforeBodyRead(ServletServerHttpRequest request, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        System.out.println("beforeBodyRead");
        return RequestBodyAdvice.super.beforeBodyRead(request, parameter, targetType, converterType);
    }

    @Override
    public Object afterBodyRead(Object body, ServletServerHttpRequest inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        System.out.println("afterBodyRead");
        return RequestBodyAdvice.super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }
}
