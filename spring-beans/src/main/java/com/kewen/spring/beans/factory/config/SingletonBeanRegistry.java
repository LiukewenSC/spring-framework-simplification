package com.kewen.spring.beans.factory.config;

import com.kewen.spring.core.lang.Nullable;

/**
 * @descrpition 获取单例模式bean注册信息
 * @author kewen
 * @since 2023-02-07
 */
public interface SingletonBeanRegistry {
    void registerSingleton(String beanName, Object singletonObject);
    @Nullable
    Object getSingleton(String beanName);

    boolean containsSingleton(String beanName);

    String[] getSingletonNames();

    /**
     * @see #registerSingleton
     * @see com.kewen.spring.beans.factory.support.BeanDefinitionRegistry#getBeanDefinitionCount
     * @see com.kewen.spring.beans.factory.ListableBeanFactory#getBeanDefinitionCount
     */
    int getSingletonCount();

}
