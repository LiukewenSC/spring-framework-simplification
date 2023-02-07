package com.kewen.spring.beans.factory;

import com.kewen.spring.beans.exception.BeansException;
import com.kewen.spring.beans.factory.config.BeanPostProcessor;
import com.kewen.spring.beans.factory.config.SingletonBeanRegistry;

public interface ConfigurableListableBeanFactory extends ConfigurableBeanFactory,ListableBeanFactory, SingletonBeanRegistry {


    /**
     * 冻结bean的定义，不再添加beanDefinition
     */
    void freezeConfiguration();

    /**
     * 初始化单例bean
     * @throws BeansException
     */
    void preInstantiateSingletons() throws BeansException;
}
