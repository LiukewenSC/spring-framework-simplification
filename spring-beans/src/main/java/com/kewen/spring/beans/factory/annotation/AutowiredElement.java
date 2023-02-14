package com.kewen.spring.beans.factory.annotation;

import com.kewen.spring.beans.factory.BeanFactory;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-02-15
 */
public abstract class AutowiredElement {
    private final Class<?> autowiredClass;
    private final boolean required;
    private final String qualifier;


    public AutowiredElement(Class<?> autowiredType,boolean required, String qualifier ) {
        this.autowiredClass = autowiredType;
        this.required = required;
        this.qualifier = qualifier;
    }


    /**
     * 填充bean属性
     * @param beanFactory
     * @param bean
     * @return
     */
    public Object fillup(BeanFactory beanFactory,Object bean){
        Object result;
        if (qualifier ==null){
            result = beanFactory.getBean(autowiredClass);
        } else {
            result =  beanFactory.getBean(qualifier,autowiredClass);
        }
        setValue(bean,result);
        return result;
    }
    protected abstract void setValue(Object bean, Object param);


    public boolean isRequired() {
        return required;
    }

    public String getQualifier() {
        return qualifier;
    }

    public Class getAutowiredClass() {
        return autowiredClass;
    }
}
