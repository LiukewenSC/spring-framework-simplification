package com.kewen.spring.beans.factory;

import com.kewen.spring.beans.factory.config.BeanPostProcessor;

/**
 * @descrpition 可配置的beanfactory
 * @author kewen
 * @since 2023-02-07 10:11
 */
public interface ConfigurableBeanFactory extends BeanFactory {

    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);
}
