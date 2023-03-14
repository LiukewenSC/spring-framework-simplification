package com.kewen.spring.http.converter;

import cn.hutool.core.io.IoUtil;
import com.alibaba.fastjson.util.IOUtils;
import com.kewen.spring.http.HttpOutputMessage;
import com.kewen.spring.http.server.ServletServerHttpRequest;

import java.io.IOException;
import java.io.InputStream;
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

    @Override
    public String read(Class<? extends String> clazz, ServletServerHttpRequest inputMessage) throws IOException {
        InputStream body = inputMessage.getBody();
        String read = IoUtil.read(body, "UTF-8");
        body.close();
        return read;
    }
}
