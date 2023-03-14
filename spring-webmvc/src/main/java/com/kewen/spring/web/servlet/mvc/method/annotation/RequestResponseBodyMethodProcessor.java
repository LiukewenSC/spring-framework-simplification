package com.kewen.spring.web.servlet.mvc.method.annotation;

import cn.hutool.core.annotation.AnnotationUtil;
import com.kewen.spring.core.MethodParameter;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.core.util.Assert;
import com.kewen.spring.http.MediaType;
import com.kewen.spring.http.converter.GenericHttpMessageConverter;
import com.kewen.spring.http.converter.HttpMessageConverter;
import com.kewen.spring.http.converter.StringHttpMessageConverter;
import com.kewen.spring.http.server.ServletServerHttpRequest;
import com.kewen.spring.http.server.ServletServerHttpResponse;
import com.kewen.spring.web.bind.annotation.RequestBody;
import com.kewen.spring.web.bind.annotation.ResponseBody;
import com.kewen.spring.web.context.request.NativeWebRequest;
import com.kewen.spring.web.method.annotation.AbstractNamedValueMethodArgumentResolver;
import com.kewen.spring.web.method.support.HandlerMethodReturnValueHandler;
import com.kewen.spring.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.List;

/**
 * @descrpition  @ResponseBody处理器
 * @author kewen
 * @since 2023-03-09
 */
public class RequestResponseBodyMethodProcessor extends AbstractMessageConverterMethodProcessor  {



    /**
     * 这里稍微和原框架不一样，原框架是把 requestBodyAdvice 和 responseBodyAdvice组合在一起利用List<Object>传入再判定返回
     * 现在就分开，要明确一点
     * @param messageConverters
     * @param requestBodyAdvice
     * @param responseBodyAdvice
     */
    public RequestResponseBodyMethodProcessor(List<HttpMessageConverter<?>> messageConverters,
                                              List<RequestBodyAdvice<Object>> requestBodyAdvice ,
                                              List<ResponseBodyAdvice<Object>> responseBodyAdvice) {
        super(messageConverters,new RequestResponseBodyAdviceChain(requestBodyAdvice,responseBodyAdvice));
    }

    @Override
    public boolean supportsReturnType(MethodParameter parameter) {
        //这里和原来框架不一样，此处更容易理解
        return AnnotationUtil.hasAnnotation(parameter.getAnnotatedElement(), ResponseBody.class)
                || AnnotationUtil.hasAnnotation(parameter.getDeclaringClass(), ResponseBody.class);
    }

    @Override
    public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
        mavContainer.setRequestHandled(true);

        //组装servlet相关的请求和返回数据，并执行数据转换
        ServletServerHttpRequest inputMessage = createInputMessage(webRequest);
        ServletServerHttpResponse outputMessage = createOutputMessage(webRequest);

        //向response中写数据
        writeWithMessageConverters(returnValue, returnType, inputMessage, outputMessage);
    }
    protected ServletServerHttpRequest createInputMessage(NativeWebRequest webRequest) {
        HttpServletRequest servletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        Assert.state(servletRequest != null, "No HttpServletRequest");
        return new ServletServerHttpRequest(servletRequest);
    }
    protected ServletServerHttpResponse createOutputMessage(NativeWebRequest webRequest) {
        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        Assert.state(response != null, "No HttpServletResponse");
        return new ServletServerHttpResponse(response);
    }




}
