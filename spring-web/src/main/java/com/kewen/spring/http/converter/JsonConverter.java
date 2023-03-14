package com.kewen.spring.http.converter;

import com.alibaba.fastjson.JSONObject;
import com.kewen.spring.beans.BeanUtils;
import com.kewen.spring.http.HttpOutputMessage;
import com.kewen.spring.http.MediaType;
import com.kewen.spring.http.server.ServletServerHttpRequest;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author kewen 对象序列化转换器，序列化为json数据
 * @descrpition
 * @since 2023-03-14
 */
public class JsonConverter extends AbstractHttpMessageConverter<Object>{

    @Override
    protected boolean supports(Class<?> clazz) {
        return !BeanUtils.isSimpleValueType(clazz);
    }

    @Override
    protected void writeInternal(Object o, HttpOutputMessage httpOutputMessage) throws IOException {
        OutputStream outputStream = httpOutputMessage.getBody();
        byte[] bytes = JSONObject.toJSONBytes(o);
        outputStream.write(bytes);
    }

    @Override
    public Object read(Class<?> clazz, ServletServerHttpRequest inputMessage) throws IOException {
        InputStream body = inputMessage.getBody();
        Object object = JSONObject.parseObject(body, clazz);
        body.close();
        return object;
    }
}
