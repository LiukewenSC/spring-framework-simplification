package com.kewen.spring.web.method;

import com.kewen.spring.core.MethodParameter;

import java.lang.reflect.Method;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-07
 */
public class HandlerMethod {
    protected Object bean;
    protected Method method;
    private final MethodParameter[] parameters;

    public HandlerMethod(Object bean, Method method) {
        this.bean = bean;
        this.method = method;
        this.parameters=initMethodParameters();
    }

    public HandlerMethod(HandlerMethod handlerMethod) {
        this.bean=handlerMethod.bean;
        this.method=handlerMethod.method;
        this.parameters=handlerMethod.parameters;
    }
    private MethodParameter[] initMethodParameters() {
        int count = this.method.getParameterCount();
        MethodParameter[] result = new MethodParameter[count];
        for (int i = 0; i < count; i++) {
            result[i] = new HandlerMethodParameter(i);
        }
        return result;
    }

    public Object getBean() {
        return bean;
    }

    public Method getMethod() {
        return method;
    }
    public MethodParameter[] getMethodParameters() {
        return this.parameters;
    }
    private class HandlerMethodParameter extends MethodParameter{
        public HandlerMethodParameter(int index) {
            super(HandlerMethod.this.method, index);
        }
    }
}
