package com.kewen.spring.beans.factory.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-02-15
 */
public class AutowiredMethodElement extends AutowiredElement {
    private final Method method;

    public AutowiredMethodElement(Method method, boolean required,String qualifier) {
        super(method.getParameters()[0].getType(),required,qualifier);
        this.method=method;
    }

    @Override
    protected void setValue(Object bean, Object param) {
        try {
            method.invoke(bean,param);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
