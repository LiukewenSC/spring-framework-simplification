package com.kewen.spring.beans;

/**
 * @descrpition 包装的bean
 * @author kewen
 * @since 2023-02-10 16:22
 */
public interface BeanWrapper {

    Object getWrappedInstance();

    Class<?> getWrappedClass();

}
