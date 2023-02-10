package com.kewen.spring.beans.factory;

/**
 * @author kewen
 * @descrpition 用以解决循环依赖和动态代理的接口
 * @since 2023-02-10 16:44
 */
public interface SmartInstantiationAwareBeanPostProcessor {
    Object getEarlyBeanReference(Object exposedObject, String beanName);
}
