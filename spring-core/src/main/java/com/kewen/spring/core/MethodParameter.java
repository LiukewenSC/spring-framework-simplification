package com.kewen.spring.core;

import com.kewen.spring.core.lang.Nullable;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Optional;

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
    public MethodParameter nestedIfOptional() {
        //此处是查内嵌的，这里就简化不查了，直接返回当前对象，目的是初始化parameter
        //return (getParameterType() == Optional.class ? nested() : this);
        getParameter();
        return this;
    }
    public Class<?> getNestedParameterType() {
        if (this.nestingLevel > 1) {
           //此处有内嵌层级大于1的逻辑，不管先
            throw new RuntimeException("");
        }
        else {
            return getParameterType();
        }
    }

    public <A extends Annotation> boolean hasParameterAnnotation(Class<A> annotationType) {
        return (getParameterAnnotation(annotationType) != null);
    }
    @Nullable
    public <A extends Annotation> A getParameterAnnotation(Class<A> annotationType) {
        Annotation[] anns = getParameterAnnotations();
        for (Annotation ann : anns) {
            if (annotationType.isInstance(ann)) {
                return (A) ann;
            }
        }
        return null;
    }
    public Annotation[] getParameterAnnotations() {
        Annotation[] paramAnns = this.parameterAnnotations;
        if (paramAnns == null) {
            Annotation[][] annotationArray = this.executable.getParameterAnnotations();
            int index = this.parameterIndex;
            paramAnns = annotationArray[index];
            this.parameterAnnotations = paramAnns;
        }
        return paramAnns;
    }
}
