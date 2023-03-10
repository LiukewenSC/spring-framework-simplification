package com.kewen.spring.http.converter;

import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.http.MediaType;
import com.kewen.spring.http.server.ServletServerHttpResponse;

import java.lang.reflect.Type;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-10
 */
public interface GenericHttpMessageConverter<T> extends HttpMessageConverter<T>{
    /**
     * 可以写值的
     * @param type
     * @param clazz
     * @param mediaType
     * @return
     */
    boolean canWrite(@Nullable Type type, Class<?> clazz, @Nullable MediaType mediaType);

    void write(T body, Type targetType, MediaType selectedMediaType, ServletServerHttpResponse outputMessage);
}
