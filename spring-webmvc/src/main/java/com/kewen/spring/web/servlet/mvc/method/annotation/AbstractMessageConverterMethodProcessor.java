package com.kewen.spring.web.servlet.mvc.method.annotation;

import cn.hutool.core.io.IoUtil;
import com.kewen.spring.core.MethodParameter;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.http.MediaType;
import com.kewen.spring.http.converter.GenericHttpMessageConverter;
import com.kewen.spring.http.converter.HttpMessageConverter;
import com.kewen.spring.http.server.ServletServerHttpRequest;
import com.kewen.spring.http.server.ServletServerHttpResponse;
import com.kewen.spring.web.context.request.NativeWebRequest;
import com.kewen.spring.web.method.support.HandlerMethodArgumentResolver;
import com.kewen.spring.web.method.support.HandlerMethodReturnValueHandler;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @descrpition 提供转换的公共方法，最终都调用到messageConverters进行处理
 * @author kewen
 * @since 2023-03-14
 */
public abstract class AbstractMessageConverterMethodProcessor implements HandlerMethodArgumentResolver, HandlerMethodReturnValueHandler {

    protected final List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
    private final RequestResponseBodyAdviceChain advice;

    public AbstractMessageConverterMethodProcessor(List<HttpMessageConverter<?>> messageConverters, RequestResponseBodyAdviceChain advice) {
        this.advice = advice;
        this.messageConverters.addAll(messageConverters);
    }


    /*-------------------------------------------------请求读数据-----------------------------------------------*/

    /**
     * 读取数据 并调用前置后置方法
     */
    @Nullable
    protected <T> Object readWithMessageConverters(NativeWebRequest webRequest, MethodParameter parameter,
                                                   Type targetType) throws IOException {

        HttpServletRequest httpServletRequest = webRequest.getNativeRequest(HttpServletRequest.class);
        ServletServerHttpRequest inputMessage =new ServletServerHttpRequest(httpServletRequest);


        //ContextType相关的处理，不影响主流程,不管


        //判定并写数据
        Class<T> targetClass = (targetType instanceof Class ? (Class<T>) targetType : null);
        MediaType contentType =null;
        Object body = null;
        for (HttpMessageConverter<?> converter : this.messageConverters) {

            Class<HttpMessageConverter<?>> converterType = (Class<HttpMessageConverter<?>>) converter.getClass();

            //判定是否可读，精简了一部分
            boolean  canRead = converter.canRead(targetClass,contentType);
            if (canRead){
                ServletServerHttpRequest toUse = advice.beforeBodyRead(inputMessage, parameter, targetType, converterType);

                body = ((HttpMessageConverter<T>) converter).read(targetClass,toUse);

                body = advice.afterBodyRead(body,inputMessage,parameter,targetType,converterType);
            }

        }
        return body;

    }









    /*-------------------------------------------------返回写数据-----------------------------------------------*/

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
            body =value;
            valueType = returnType.getParameterType();
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
                if (!canWrite){
                    continue;
                }
                body = advice.beforeBodyWrite(
                        body, returnType, selectedMediaType,
                        (Class<? extends HttpMessageConverter<?>>) converter.getClass(),
                        inputMessage, outputMessage
                );
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
    protected void addContentDispositionHeader(ServletServerHttpRequest inputMessage, ServletServerHttpResponse outputMessage) {

    }


}
