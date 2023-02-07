package com.kewen.spring.beans.factory.config;

import com.kewen.spring.beans.exception.BeansException;
import com.kewen.spring.beans.factory.ConfigurableListableBeanFactory;

/**
 * @descrpition bean factory后处理器，beanDefinition加载完成后处理
 *  在 beanDefinition加载完成 和bean实例化之间
 * @author kewen
 * @since 2023-02-07 10:52
 */
public interface BeanFactoryPostProcessor {
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;
}
