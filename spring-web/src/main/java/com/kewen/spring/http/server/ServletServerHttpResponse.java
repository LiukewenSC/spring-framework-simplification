package com.kewen.spring.http.server;

import com.kewen.spring.http.HttpHeaders;
import com.kewen.spring.http.HttpMessage;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLConnection;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-09
 */
public class ServletServerHttpResponse implements HttpMessage, ServerHttpResponse {
    HttpServletResponse servletResponse;

    private boolean headersWritten = false;
    private final HttpHeaders headers;
    public ServletServerHttpResponse(HttpServletResponse servletResponse) {
        this.servletResponse = servletResponse;
        this.headers=new ServletResponseHttpHeaders();
    }

    @Override
    public HttpHeaders getHeaders() {
        return (this.headersWritten ? HttpHeaders.readOnlyHttpHeaders(this.headers) : this.headers);
    }

    @Override
    public OutputStream getBody() throws IOException {
        return servletResponse.getOutputStream();
    }
}
