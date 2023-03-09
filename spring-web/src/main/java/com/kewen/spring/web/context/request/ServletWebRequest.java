package com.kewen.spring.web.context.request;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-08
 */
public class ServletWebRequest implements NativeWebRequest{
    HttpServletRequest request;
    HttpServletResponse response;

    public ServletWebRequest(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
    }
}
