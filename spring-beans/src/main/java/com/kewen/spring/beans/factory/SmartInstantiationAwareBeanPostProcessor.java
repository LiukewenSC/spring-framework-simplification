package com.kewen.spring.beans.factory;

import com.kewen.spring.beans.factory.config.InstantiationAwareBeanPostProcessor;

/**
 * @author kewen
 * @descrpition 用以解决循环依赖和动态代理的接口
 * @since 2023-02-10
 */
public interface SmartInstantiationAwareBeanPostProcessor extends InstantiationAwareBeanPostProcessor {
    default Object getEarlyBeanReference(Object bean, String beanName){
        return bean;
    }
}
