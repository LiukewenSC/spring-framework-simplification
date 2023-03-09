package com.kewen.spring.web.bind;

import com.kewen.spring.web.util.NestedServletException;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-09
 */
public class ServletRequestBindingException extends NestedServletException {
    public ServletRequestBindingException() {
    }

    public ServletRequestBindingException(String message) {
        super(message);
    }

    public ServletRequestBindingException(String message, Throwable rootCause) {
        super(message, rootCause);
    }

    public ServletRequestBindingException(Throwable rootCause) {
        super(rootCause);
    }
}
