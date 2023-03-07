package com.kewen.spring.beans.factory;

import com.kewen.spring.beans.exception.BeansException;

/**
 * @descrpition Bean的创建工厂，主要用于创建bean
 * @author kewen
 * @since 2023-02-06 17:41
 */
public interface AutowireCapableBeanFactory extends BeanFactory{

    int AUTOWIRE_NO = 0;


    int AUTOWIRE_BY_NAME = 1;


    int AUTOWIRE_BY_TYPE = 2;


    int AUTOWIRE_CONSTRUCTOR = 3;

    @Deprecated
    int AUTOWIRE_AUTODETECT = 4;

    String ORIGINAL_INSTANCE_SUFFIX = ".ORIGINAL";


    //-------------------------------------------------------------------------
    // Typical methods for creating and populating external bean instances
    //-------------------------------------------------------------------------


    <T> T createBean(Class<T> beanClass) throws BeansException;


    Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
            throws BeansException;


    Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
            throws BeansException;

}
