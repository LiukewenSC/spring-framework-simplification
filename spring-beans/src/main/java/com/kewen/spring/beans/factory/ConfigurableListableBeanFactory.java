package com.kewen.spring.beans.factory;

import com.kewen.spring.beans.exception.BeansException;

/**
 * 配置接口将由大多数可列出的bean工厂实现。
 * 除了ConfigurableBeanFactory之外，它还提供了分析和修改bean定义以及预实例化单例的工具。
 */
public interface ConfigurableListableBeanFactory extends ConfigurableBeanFactory,ListableBeanFactory {


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
