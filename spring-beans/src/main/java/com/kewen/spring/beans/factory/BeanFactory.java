package com.kewen.spring.beans.factory;

public interface BeanFactory {

    Object getBean(String beanName);
    <T> T  getBean(String beanName,Class<T> clazz);
    <T> T  getBean(Class<T> clazz);
    boolean containsBean(String beanName);

}
