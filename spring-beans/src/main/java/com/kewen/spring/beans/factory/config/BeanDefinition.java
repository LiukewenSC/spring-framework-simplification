package com.kewen.spring.beans.factory.config;

import com.kewen.spring.beans.MutablePropertyValues;
import com.kewen.spring.core.lang.Nullable;

/**
 * @descrpition bean定义
 * @author kewen
 * @since 2023-02-06
 */
public interface BeanDefinition {


    void setParentName(@Nullable String parentName);


    @Nullable
    String getParentName();

    void setBeanClassName(@Nullable String beanClassName);

    @Nullable
    String getBeanClassName();

    Class<?> getBeanClass();
    void setBeanClass(@Nullable Class<?> beanClass) ;
    void setScope(@Nullable String scope);

    @Nullable
    String getScope();

    void setLazyInit(boolean lazyInit);

    boolean getLazyInit();

    void setDependsOn(@Nullable String... dependsOn);

    @Nullable
    String[] getDependsOn();

    void setAutowireCandidate(boolean autowireCandidate);

    boolean isAutowireCandidate();

    void setPrimary(boolean primary);

    boolean isPrimary();

    void setFactoryBeanName(@Nullable String factoryBeanName);

    @Nullable
    String getFactoryBeanName();

    void setFactoryMethodName(@Nullable String factoryMethodName);

    @Nullable
    String getFactoryMethodName();

    boolean isSingleton();

    MutablePropertyValues getPropertyValues();
}
