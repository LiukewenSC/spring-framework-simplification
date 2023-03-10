package com.kewen.spring.web.method;

import com.kewen.spring.core.MethodParameter;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.http.HttpStatus;

import java.lang.reflect.Method;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-07
 */
public class HandlerMethod {
    protected Object bean;
    protected Method method;

    @Nullable
    private HttpStatus responseStatus;
    @Nullable
    private String responseStatusReason;
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

    public String getResponseStatusReason() {
        return responseStatusReason;
    }

    public HttpStatus getResponseStatus() {
        return responseStatus;
    }

    public MethodParameter getReturnValueType(@Nullable Object returnValue) {
        return new ReturnValueMethodParameter(returnValue);
    }

    /**
     * 方法参数处理器
     */
    private class HandlerMethodParameter extends MethodParameter{
        public HandlerMethodParameter(int index) {
            super(HandlerMethod.this.method, index);
        }
    }

    /**
     * 记录返回值的HandlerMethod
     */
    private class ReturnValueMethodParameter extends HandlerMethodParameter {

        @Nullable
        private final Object returnValue;

        public ReturnValueMethodParameter(@Nullable Object returnValue) {
            super(-1);
            this.returnValue = returnValue;
        }

        @Override
        public Class<?> getParameterType() {
            return (this.returnValue != null ? this.returnValue.getClass() : super.getParameterType());
        }
    }
}
