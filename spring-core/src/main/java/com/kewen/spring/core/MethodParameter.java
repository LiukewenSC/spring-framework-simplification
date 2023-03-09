package com.kewen.spring.core;

import com.kewen.spring.core.lang.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Executable;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

/**
 * @descrpition 方法参数
 * @author kewen
 * @since 2023-03-08
 */
public class MethodParameter {


    /**
     * 执行方法，一般为Method
     */
    private final Executable executable;

    /**
     * 参数位置索引0开始
     */
    private final int parameterIndex;

    /**
     * 参数值
     */
    @Nullable
    private volatile Parameter parameter;

    /**
     * 内嵌的层级，一般为1，不嵌套
     */
    private int nestingLevel;

    /**
     * 参数类型，懒加载，坑得很，在getParameterType中初始化值
     */
    @Nullable
    private volatile Class<?> parameterType;


    /**
     * 参数注解，也是在getParameterAnnotations再从executable中加载
     */
    @Nullable
    private volatile Annotation[] parameterAnnotations;


    public MethodParameter(Executable executable, int parameterIndex) {
        this.executable = executable;
        this.parameterIndex = parameterIndex;
    }
    public MethodParameter(MethodParameter original) {
        this.executable = original.executable;
        this.parameterIndex = original.parameterIndex;
        this.parameter = original.parameter;
        this.nestingLevel = original.nestingLevel;
        this.parameterType = original.parameterType;
        this.parameterAnnotations = original.parameterAnnotations;
    }

    public Class<?> getDeclaringClass() {
        return this.executable.getDeclaringClass();
    }
    public Member getMember() {
        return this.executable;
    }
    public AnnotatedElement getAnnotatedElement() {
        return this.executable;
    }
    public Executable getExecutable() {
        return this.executable;
    }
    public Parameter getParameter() {
        if (this.parameterIndex < 0) {
            throw new IllegalStateException("Cannot retrieve Parameter descriptor for method return type");
        }
        Parameter parameter = this.parameter;
        if (parameter == null) {
            parameter = getExecutable().getParameters()[this.parameterIndex];
            this.parameter = parameter;
        }
        return parameter;
    }
    public int getParameterIndex() {
        return this.parameterIndex;
    }
    public Class<?> getParameterType() {
        Class<?> paramType = this.parameterType;
        if (paramType != null) {
            return paramType;
        }
        if (paramType == null) {
            paramType = computeParameterType();
        }
        this.parameterType = paramType;
        return paramType;
    }
    private Class<?> computeParameterType() {
        return this.executable.getParameterTypes()[this.parameterIndex];
    }
}
