package com.kewen.spring.http.converter;

import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.http.HttpOutputMessage;
import com.kewen.spring.http.MediaType;
import com.kewen.spring.http.server.ServletServerHttpResponse;

import java.io.IOException;

/**
 * @descrpition 消息处理转换器
 * @author kewen
 * @since 2023-03-10
 */
public interface HttpMessageConverter<T> {
    /**
     * 可以写值的
     * @param clazz
     * @param mediaType
     * @return
     */
    boolean canWrite(Class<?> clazz, @Nullable MediaType mediaType);

    void write(T t, @Nullable MediaType contentType, HttpOutputMessage outputMessage) throws IOException;

}
