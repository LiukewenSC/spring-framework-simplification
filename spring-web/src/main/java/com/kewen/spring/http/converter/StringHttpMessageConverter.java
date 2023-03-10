package com.kewen.spring.http.converter;

import com.kewen.spring.http.HttpOutputMessage;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

/**
 * @descrpition string类型的转换器
 * @author kewen
 * @since 2023-03-10
 */
public class StringHttpMessageConverter extends AbstractHttpMessageConverter<String>{

    @Override
    protected boolean supports(Class<?> clazz) {
        return clazz == String.class;
    }

    @Override
    protected void writeInternal(String s, HttpOutputMessage httpOutputMessage) throws IOException {
        Writer writer = new OutputStreamWriter(httpOutputMessage.getBody(), StandardCharsets.UTF_8);
        writer.write(s);
        writer.flush();
    }
}
