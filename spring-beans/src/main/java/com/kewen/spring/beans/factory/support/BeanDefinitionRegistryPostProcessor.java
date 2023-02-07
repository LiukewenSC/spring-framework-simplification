package com.kewen.spring.beans.factory.support;

import com.kewen.spring.beans.exception.BeansException;
import com.kewen.spring.beans.factory.config.BeanFactoryPostProcessor;

/**
 * @descrpition BeanDefinitionRegistry的后处理器，可以再此添加bean等信息
 * @author kewen
 * @since 2023-02-07 14:06
 */
public interface BeanDefinitionRegistryPostProcessor extends BeanFactoryPostProcessor {
    /**
     * 注册器处理
     * 在 BeanDefinition加载完成和Bean实例化之间，并且优于 BeanFactoryPostProcessor
     * @param registry
     * @throws BeansException
     */
    void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException;
}
