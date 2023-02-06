package com.kewen.spring.beans.exception;
/**
 * @descrpition 
 * @author kewen
 * @since 2023-02-06 15:09
 */
public class BeansException extends RuntimeException {
    public BeansException(String message) {
        super(message);
    }

    public BeansException(String message, Throwable cause) {
        super(message, cause);
    }
}
