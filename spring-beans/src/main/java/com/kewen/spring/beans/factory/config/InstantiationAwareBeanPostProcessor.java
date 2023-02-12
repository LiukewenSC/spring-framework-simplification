package com.kewen.spring.beans.factory.config;

import com.kewen.spring.beans.exception.BeansException;

/**
 *
 * @descrpition 以下是官方翻译过来的说明，主要用于实现代理，或池化(Druid实现了此接口)
 * BeanPostProcessor的子接口，它添加实例化前回调和实例化后但显式属性设置或自动装配发生之前的回调。
 * 通常用于抑制特定目标bean的默认实例化，例如使用特殊的TargetSources创建代理(池化目标、惰性初始化目标等)，
 * 或者实现附加的所有注入策略，如字段注入。注意:该接口是一个特殊用途的接口，主要用于框架内的内部使用。
 * 建议尽可能实现普通的BeanPostProcessor接口，或者从InstantiationAwareBeanPostProcessorAdapter派生，
 * 以避免对该接口的扩展。
 * @author kewen
 * @since 2023-02-10 13:59
 */
public interface InstantiationAwareBeanPostProcessor extends BeanPostProcessor {

    default Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        return null;
    }

    /**
     *  在注入bean属性之前 {populateBean()} 执行此方法，并必须返回true，若返回false则结束注入
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    default boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        return true;
    }

}
