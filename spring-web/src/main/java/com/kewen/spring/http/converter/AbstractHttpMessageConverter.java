package com.kewen.spring.http.converter;

import com.kewen.spring.http.HttpHeaders;
import com.kewen.spring.http.HttpOutputMessage;
import com.kewen.spring.http.MediaType;
import com.kewen.spring.http.server.ServletServerHttpRequest;

import java.io.IOException;

/**
 * @descrpition 抽象的类型转换器，主要用于设置header头和关流使用
 * @author kewen
 * @since 2023-03-10
 */
public abstract class AbstractHttpMessageConverter<T> implements HttpMessageConverter<T>  {
    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        //此处实际上还要判断contentType，先就这样
        return supports(clazz);
    }

    @Override
    public boolean canRead(Class<?> clazz, MediaType mediaType) {
        return supports(clazz);
    }

    protected abstract boolean supports(Class<?> clazz);
    @Override
    public void write(T t, MediaType contentType, HttpOutputMessage outputMessage) throws IOException {

        //设置header
        final HttpHeaders headers = outputMessage.getHeaders();
        addDefaultHeaders(headers, t, contentType);
        //写数据
        writeInternal(t, outputMessage);
        //关流
        outputMessage.getBody().flush();
    }

    private void addDefaultHeaders(HttpHeaders headers, T t, MediaType contentType) {
        headers.setContentType(contentType);
    }

    protected  abstract void writeInternal(T t, HttpOutputMessage httpOutputMessage) throws IOException;
}
