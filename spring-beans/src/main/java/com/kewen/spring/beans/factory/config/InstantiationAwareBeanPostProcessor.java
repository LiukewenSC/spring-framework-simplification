package com.kewen.spring.beans.factory.config;

import com.kewen.spring.beans.PropertyValues;
import com.kewen.spring.beans.exception.BeansException;
import com.kewen.spring.core.lang.Nullable;

import java.beans.PropertyDescriptor;

/**
 *
 * @descrpition 以下是官方翻译过来的说明，主要用于实现代理，或池化(Druid实现了此接口)
 * BeanPostProcessor的子接口，它添加实例化前回调和实例化后但显式属性设置或自动装配发生之前的回调。
 * 通常用于抑制特定目标bean的默认实例化，例如使用特殊的TargetSources创建代理(池化目标、惰性初始化目标等)，
 * 或者实现附加的所有注入策略，如字段注入。注意:该接口是一个特殊用途的接口，主要用于框架内的内部使用。
 * 建议尽可能实现普通的BeanPostProcessor接口，或者从InstantiationAwareBeanPostProcessorAdapter派生，
 * 以避免对该接口的扩展。
 * @author kewen
 * @since 2023-02-10
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

    /**
     * 在工厂将给定的属性值应用到给定的bean之前，对它们进行后处理，不需要任何属性描述符。
     * 如果实现提供了自定义的postProcessPropertyValues实现，则应返回null(默认值)，否则应返回pvs。
     * 在此接口的未来版本(删除了postProcessPropertyValues)中，默认实现将直接返回给定的pv。
     * Params: pvs——工厂将要应用的属性值(从不为空)bean—已创建的bean实例，
     * 但尚未设置其属性返回:应用于给定bean的实际属性值(可以是传入的PropertyValues实例)，或者null，
     * 它继续使用现有的属性，但特别地继续调用postProcessPropertyValues(需要为当前bean类初始化PropertyDescriptors)
     * 抛出:BeansException——以防出现错误
     * @param pvs
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Nullable
    default PropertyValues postProcessProperties(@Nullable PropertyValues pvs, Object bean, String beanName)
            throws BeansException {

        return null;
    }

    default PropertyValues postProcessPropertyValues(
            PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {

        return pvs;
    }

}
