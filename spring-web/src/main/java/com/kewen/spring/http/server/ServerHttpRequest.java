package com.kewen.spring.http.server;

import java.io.IOException;
import java.io.InputStream;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-10
 */
public interface ServerHttpRequest {
    InputStream getBody() throws IOException;
}
