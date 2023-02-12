package com.kewen.spring.beans;


import java.beans.PropertyDescriptor;

/**
 * @descrpition 包装的bean
 * @author kewen
 * @since 2023-02-10 16:22
 */
public interface BeanWrapper {

    Object getWrappedInstance();

    Class<?> getWrappedClass();

    /**
     * 返回属性的描述，用于注入属性时获取相关信息
     * @return
     */
    PropertyDescriptor[] getPropertyDescriptors();

    /**
     * 返回属性的描述，用于注入属性时获取相关信息
     * @param propertyName
     * @return
     */
    PropertyDescriptor getPropertyDescriptor(String propertyName);
}
