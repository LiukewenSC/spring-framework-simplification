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
public class RequestResponseBodyMethodProcessor implements HandlerMethodReturnValueHandler {


    protected final List<HttpMessageConverter<?>> messageConverters;
    private final RequestResponseBodyAdviceChain advice;

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
        this.messageConverters = messageConverters;
        this.advice = new RequestResponseBodyAdviceChain(requestBodyAdvice,responseBodyAdvice);
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


    /**
     * 写数据，此处非常复杂，json的序列化也在这里
     * @param value
     * @param returnType
     * @param inputMessage
     * @param outputMessage
     * @param <T>
     * @throws IOException
     */
    protected <T> void writeWithMessageConverters(@Nullable T value, MethodParameter returnType,
                    ServletServerHttpRequest inputMessage, ServletServerHttpResponse outputMessage)
            throws IOException {

        Object body;
        Class<?> valueType;
        Type targetType;

        if (value instanceof CharSequence) {
            body = value.toString();
            valueType = String.class;
            targetType = String.class;
        } else {
            body =null;
            valueType = null;
            targetType = null;
            //body = value;
            //valueType = (value != null ? value.getClass() : returnType.getParameterType());
            //targetType = GenericTypeResolver.resolveType(getGenericType(returnType), returnType.getContainingClass());
        }

        //解析了一系列的ContentType，直接不管，写死
        MediaType selectedMediaType = MediaType.parseMediaType("text/plain");


        if (selectedMediaType !=null){
            for (HttpMessageConverter<?> converter : messageConverters) {

                // 通过GenericHttpMessageConverter 或 HttpMessageConverter 写回数据至response中

                //判定是否可写，稍微改了一点，方便阅读，逻辑不变
                boolean canWrite;
                GenericHttpMessageConverter<Object> genericConverter =null;
                if (converter instanceof GenericHttpMessageConverter){
                    genericConverter = (GenericHttpMessageConverter<Object>) converter;
                    canWrite = genericConverter.canWrite(targetType,valueType,selectedMediaType);
                } else {
                    canWrite = converter.canWrite(valueType,selectedMediaType);
                }

                //可以写的话则写返回前的最后数据
                if (canWrite){
                    body = advice.beforeBodyWrite(
                            body, returnType, selectedMediaType,
                            (Class<? extends HttpMessageConverter<?>>) converter.getClass(),
                            inputMessage, outputMessage
                    );
                }
                //写数据操作
                if (body != null) {
                    //检查文件扩展
                    addContentDispositionHeader(inputMessage, outputMessage);
                    if (genericConverter != null) {
                        genericConverter.write(body, targetType, selectedMediaType, outputMessage);
                    }
                    else {
                        ((HttpMessageConverter) converter).write(body, selectedMediaType, outputMessage);
                    }
                }
                else {
                    System.out.println(("Nothing to write: null body"));
                }
                return;
            }
        }

    }

    /**
     * 不知道干嘛的，不管
     * 检查路径是否有文件扩展名，以及该扩展名是否在安全扩展名列表中或显式注册。
     * 如果不是，并且状态在2xx范围内，则添加带有安全附件文件名(“f.txt”)的“ContentDisposition”头，以防止RFD漏洞。
     */
    private void addContentDispositionHeader(ServletServerHttpRequest inputMessage, ServletServerHttpResponse outputMessage) {

    }
}
