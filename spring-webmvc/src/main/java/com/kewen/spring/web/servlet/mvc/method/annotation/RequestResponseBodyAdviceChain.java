package com.kewen.spring.web.servlet.mvc.method.annotation;

import com.kewen.spring.core.MethodParameter;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.http.MediaType;
import com.kewen.spring.http.converter.HttpMessageConverter;
import com.kewen.spring.http.server.ServerHttpRequest;
import com.kewen.spring.http.server.ServerHttpResponse;
import com.kewen.spring.http.server.ServletServerHttpRequest;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @descrpition 返回值处理链
 * @author kewen
 * @since 2023-03-10
 */
public class RequestResponseBodyAdviceChain implements RequestBodyAdvice<Object>,ResponseBodyAdvice<Object> {
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
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return false;
    }

    /**
     *
     * @param request
     * @param parameter
     * @param targetType
     * @param converterType
     * @return
     * @throws IOException
     */
    @Override
    public ServletServerHttpRequest beforeBodyRead(ServletServerHttpRequest request, MethodParameter parameter,
                                                   Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {

        for (RequestBodyAdvice advice : requestBodyAdvices) {
            if (advice.supports(parameter, targetType, converterType)) {
                request = advice.beforeBodyRead(request, parameter, targetType, converterType);
            }
        }
        return request;
    }

    @Override
    public Object afterBodyRead(Object body, ServletServerHttpRequest inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        for (RequestBodyAdvice<Object> advice : requestBodyAdvices) {
            if (advice.supports(parameter, targetType, converterType)) {
                body = advice.afterBodyRead(body,inputMessage, parameter, targetType, converterType);
            }
        }
        return body;
    }





    /*----------------------------------------写数据----------------------------------------------*/


    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * 写数据流之前的操作
     * @param body
     * @param returnType
     * @param contentType
     * @param converterType
     * @param request
     * @param response
     * @return
     */
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
