package com.kewen.spring.beans.factory.config;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kewen.spring.beans.exception.BeansException;
import com.kewen.spring.beans.factory.AutowireCapableBeanFactory;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.core.util.ClassUtils;

/**
 * @author kewen
 * @descrpition 抽象的bean定义
 * @since 2023-02-10 11:18
 */
public abstract class AbstractBeanDefinition implements BeanDefinition {

    public static final String SCOPE_DEFAULT = "";


    @Deprecated
    public static final int AUTOWIRE_AUTODETECT = AutowireCapableBeanFactory.AUTOWIRE_AUTODETECT;


    @Nullable
    private  Class<?> beanClass;

    @Nullable
    private String scope = SCOPE_DEFAULT;

    @Nullable
    private boolean lazyInit = false;

    @Nullable
    private String[] dependsOn;

    private boolean autowireCandidate = true;

    private boolean primary = false;

    @Nullable
    private String factoryBeanName;

    @Nullable
    private String factoryMethodName;


    public AbstractBeanDefinition() {

    }
    public AbstractBeanDefinition(BeanDefinition beanDefinition) {

        //简单处理，直接copy，原框架还有比较复杂的逻辑
        BeanUtil.copyProperties(beanDefinition,this);


    }

    public boolean getLazyInit() {
        return lazyInit;
    }

    public void setLazyInit(boolean lazyInit) {
        this.lazyInit = lazyInit;
    }

    @Override
    public void setBeanClassName(String beanClassName) {
        try {
            beanClass = ClassUtils.forName(beanClassName);
        } catch (ClassNotFoundException e) {
            throw new BeansException("createBeanDefinition exception ClassNotFound",e);
        }
    }

    @Override
    public String getBeanClassName() {
        return beanClass.getName();
    }

    public Class<?> getBeanClass() throws IllegalStateException {
        Class<?> beanClassObject = this.beanClass;
        if (beanClassObject == null) {
            throw new IllegalStateException("No bean class specified on bean definition");
        }
        return beanClassObject;
    }

    public void setBeanClass(@Nullable Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public String getScope() {
        return scope;
    }

    @Override
    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public String[] getDependsOn() {
        return dependsOn;
    }

    @Override
    public void setDependsOn(String[] dependsOn) {
        this.dependsOn = dependsOn;
    }

    @Override
    public boolean isAutowireCandidate() {
        return autowireCandidate;
    }

    @Override
    public void setAutowireCandidate(boolean autowireCandidate) {
        this.autowireCandidate = autowireCandidate;
    }

    @Override
    public boolean isPrimary() {
        return primary;
    }

    @Override
    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    @Override
    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    @Override
    public void setFactoryBeanName(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    @Override
    public String getFactoryMethodName() {
        return factoryMethodName;
    }

    @Override
    public void setFactoryMethodName(String factoryMethodName) {
        this.factoryMethodName = factoryMethodName;
    }


    @Override
    public boolean isSingleton() {
        return true;
    }
}
