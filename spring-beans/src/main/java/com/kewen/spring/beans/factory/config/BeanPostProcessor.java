package com.kewen.spring.beans.factory.config;

import com.kewen.spring.beans.exception.BeansException;
import com.kewen.spring.core.lang.Nullable;

/**
 * @descrpition bean后处理器，用于beandefinition加载完成后，bean的实例化过程中
 * @author kewen
 * @since 2023-02-07 9:57
 */
public interface BeanPostProcessor {
    /**
     * bean加载到容器的前置操作，实例化之前
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Nullable
    default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    /**
     * bean加载至容器的后置操作，在已经实例化bean完成后
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Nullable
    default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}
