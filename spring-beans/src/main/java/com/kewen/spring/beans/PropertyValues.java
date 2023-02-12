package com.kewen.spring.beans;

/**
 * @descrpition 属性填充值
 * @author kewen
 * @since 2023-02-12 14:04
 */
public interface PropertyValues {

    boolean isEmpty();

    /**
     * 获取属性
     * @param propertyName
     * @return
     */
    PropertyValue getPropertyValue(String propertyName);

    /**
     * 是否包含属性对应的 PropertyValue 值
     * @param propertyName
     * @return
     */
    boolean contains(String propertyName);
}
