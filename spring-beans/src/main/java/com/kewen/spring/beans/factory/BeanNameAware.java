package com.kewen.spring.beans.factory;

/**
 * @descrpition 注入BeanName
 * @author kewen
 * @since 2023-03-07
 */
public interface BeanNameAware extends Aware{

    void setBeanName(String beanName);
}
