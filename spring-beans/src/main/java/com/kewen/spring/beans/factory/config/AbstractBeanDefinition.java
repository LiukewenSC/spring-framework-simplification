package com.kewen.spring.beans.factory.config;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kewen.spring.beans.MutablePropertyValues;
import com.kewen.spring.beans.exception.BeansException;
import com.kewen.spring.beans.factory.AutowireCapableBeanFactory;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.core.util.ClassUtils;

import java.lang.reflect.Constructor;

/**
 * @author kewen
 * @descrpition 抽象的bean定义
 * @since 2023-02-10
 */
public abstract class AbstractBeanDefinition implements BeanDefinition {

    public static final String SCOPE_DEFAULT = "";


    public static final int AUTOWIRE_BY_NAME = AutowireCapableBeanFactory.AUTOWIRE_BY_NAME;

    public static final int AUTOWIRE_BY_TYPE = AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE;

    public static final int AUTOWIRE_CONSTRUCTOR = AutowireCapableBeanFactory.AUTOWIRE_CONSTRUCTOR;

    public static final int AUTOWIRE_AUTODETECT = AutowireCapableBeanFactory.AUTOWIRE_AUTODETECT;

    private MutablePropertyValues propertyValues;
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

    private int autowireMode = AUTOWIRE_AUTODETECT;

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

    /**
     * 返回注入的类型，有无参构造的直接按照类型注入，即set方法注入，其余返回预定的值；
     * @return
     */
    public int getResolvedAutowireMode(){
        if (this.autowireMode==AUTOWIRE_AUTODETECT){
            //如果有无参构造，则视为按类型注入
            Constructor<?>[] constructors = getBeanClass().getConstructors();
            for (Constructor<?> constructor : constructors) {
                if (constructor.getParameterCount() == 0) {
                    return AUTOWIRE_BY_TYPE;
                }
            }
            return AUTOWIRE_CONSTRUCTOR;
        }
        return this.autowireMode;
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

    public boolean hasPropertyValues() {
        return  propertyValues != null && propertyValues.isEmpty();
    }
    public MutablePropertyValues getPropertyValues() {
        return propertyValues;
    }

    public void setPropertyValues(MutablePropertyValues propertyValues) {
        this.propertyValues = propertyValues;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
