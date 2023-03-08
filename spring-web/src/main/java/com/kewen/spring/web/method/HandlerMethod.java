package com.kewen.spring.web.method;

import java.lang.reflect.Method;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-07
 */
public class HandlerMethod {
    private Object bean;
    private Method method;

    public HandlerMethod(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
    }

    public Object getBean() {
        return bean;
    }

    public Method getMethod() {
        return method;
    }
}
