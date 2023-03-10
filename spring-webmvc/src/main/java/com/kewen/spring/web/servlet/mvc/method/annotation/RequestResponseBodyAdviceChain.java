package com.kewen.spring.web.servlet.mvc.method.annotation;

import com.kewen.spring.core.MethodParameter;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.http.MediaType;
import com.kewen.spring.http.converter.HttpMessageConverter;
import com.kewen.spring.http.server.ServerHttpRequest;
import com.kewen.spring.http.server.ServerHttpResponse;

import java.util.List;

/**
 * @descrpition 返回值处理链
 * @author kewen
 * @since 2023-03-10
 */
public class RequestResponseBodyAdviceChain implements RequestBodyAdvice,ResponseBodyAdvice<Object> {
    List<RequestBodyAdvice<Object>> requestBodyAdvices;
    List<ResponseBodyAdvice<Object>> responseBodyAdvices;

    /**
     * 这里稍微和原框架不一样，原框架是把 requestBodyAdvice 和 responseBodyAdvice组合在一起利用List<Object>传入再判定返回
     * 现在就分开，要明确一点
     * @param requestBodyAdvice
     * @param responseBodyAdvice
     */
    public RequestResponseBodyAdviceChain(List<RequestBodyAdvice<Object>> requestBodyAdvice, List<ResponseBodyAdvice<Object>> responseBodyAdvice) {
        this.requestBodyAdvices = requestBodyAdvice;
        this.responseBodyAdvices = responseBodyAdvice;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    @Nullable
    public Object beforeBodyWrite(@Nullable Object body, MethodParameter returnType, MediaType contentType,
                                  Class<? extends HttpMessageConverter<?>> converterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

        return processBody(body, returnType, contentType, converterType, request, response);
    }

    private Object processBody(Object body, MethodParameter returnType, MediaType contentType,
           Class<? extends HttpMessageConverter<?>> converterType, ServerHttpRequest request, ServerHttpResponse response) {
        for (ResponseBodyAdvice<Object> responseBodyAdvice : responseBodyAdvices) {
            if (responseBodyAdvice.supports(returnType,converterType)){
                body = responseBodyAdvice.beforeBodyWrite(body, returnType, contentType, converterType, request, response);
            }
        }
        return body;
    }
}
