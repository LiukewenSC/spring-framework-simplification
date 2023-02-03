package com.kewen.spring.beans.exception;

public class BeanInstantiationException extends RuntimeException {
    public BeanInstantiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
