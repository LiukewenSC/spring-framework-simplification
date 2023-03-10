package com.kewen.spring.web.sample.advance;

import com.kewen.spring.context.annotation.Component;
import com.kewen.spring.core.MethodParameter;
import com.kewen.spring.http.MediaType;
import com.kewen.spring.http.converter.HttpMessageConverter;
import com.kewen.spring.http.server.ServerHttpRequest;
import com.kewen.spring.http.server.ServerHttpResponse;
import com.kewen.spring.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-10
 */
@Component
public class ResultResponseBodyAdvance implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        System.out.println(body);
        return body;
    }
}
