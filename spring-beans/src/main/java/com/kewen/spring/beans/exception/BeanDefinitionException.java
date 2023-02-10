package com.kewen.spring.beans.exception;

/**
 * @author kewen
 * @descrpition
 * @since 2023-02-10 11:21
 */
public class BeanDefinitionException extends RuntimeException {
    public BeanDefinitionException(String message) {
        super(message);
    }

    public BeanDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }
}
