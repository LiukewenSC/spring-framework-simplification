package com.kewen.spring.http;

import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.core.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @descrpition  header相关，原框架是实现的Map，这儿直接用hashmap了，方便
 * @author kewen
 * @since 2023-03-09
 */
public class HttpHeaders extends HashMap<String,String> {
    public static final String CONTENT_TYPE = "Content-Type";
    private final Map<String, String> headers;

    public HttpHeaders() {
        headers = new HashMap<>();
    }

    public HttpHeaders(Map<String, String> headers) {
        this.headers = headers;
    }


    @Nullable
    public MediaType getContentType() {
        String value = get(CONTENT_TYPE);
        return (StringUtils.hasLength(value) ? MediaType.parseMediaType(value) : null);
    }
    public void setContentType(@Nullable MediaType mediaType) {
        headers.put(CONTENT_TYPE,mediaType.type);
    }

    public static HttpHeaders readOnlyHttpHeaders(HttpHeaders headers) {
        return headers instanceof ReadOnlyHttpHeaders? headers:new ReadOnlyHttpHeaders(headers);
    }
}
