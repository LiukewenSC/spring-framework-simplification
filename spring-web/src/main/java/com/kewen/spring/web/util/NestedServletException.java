package com.kewen.spring.web.util;

import javax.servlet.ServletException;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-08
 */
public class NestedServletException extends ServletException {
    public NestedServletException() {
    }

    public NestedServletException(String message) {
        super(message);
    }

    public NestedServletException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

    public NestedServletException(Throwable rootCause) {
        super(rootCause);
    }
}
