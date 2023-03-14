package com.kewen.spring.http.converter;

import com.alibaba.fastjson.JSONObject;
import com.kewen.spring.http.HttpOutputMessage;
import com.kewen.spring.http.MediaType;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @author kewen
 * @descrpition
 * @since 2023-03-14
 */
public class JsonConverter implements HttpMessageConverter<Object>{
    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return true;
    }

    @Override
    public void write(Object t, MediaType contentType, HttpOutputMessage outputMessage) throws IOException {
        OutputStream outputStream = outputMessage.getBody();
        byte[] bytes = JSONObject.toJSONBytes(t);
        outputStream.write(bytes);
        outputStream.close();
    }
}
