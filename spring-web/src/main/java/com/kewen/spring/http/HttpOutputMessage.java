package com.kewen.spring.http;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-10
 */
public interface HttpOutputMessage  extends HttpMessage{
    OutputStream getBody() throws IOException;
}
