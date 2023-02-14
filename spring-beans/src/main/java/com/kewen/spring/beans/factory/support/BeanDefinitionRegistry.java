package com.kewen.spring.beans.factory.support;

import com.kewen.spring.beans.factory.config.BeanDefinition;

/**
 * @descrpition Bean定义注册
 * @author kewen
 * @since 2023-02-06
 */
public interface BeanDefinitionRegistry {
    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
    void registerAlias(String beanName,String[] alias);

    int getBeanDefinitionCount();

    boolean containsBeanDefinition(String beanName);

}
