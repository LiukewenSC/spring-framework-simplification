package com.kewen.spring.web.sample.service;

import com.kewen.spring.beans.factory.BeanClassLoaderAware;
import com.kewen.spring.beans.factory.BeanFactory;
import com.kewen.spring.beans.factory.BeanFactoryAware;
import com.kewen.spring.beans.factory.BeanNameAware;
import com.kewen.spring.beans.factory.InitializingBean;
import com.kewen.spring.context.annotation.Service;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-07
 */
@Service
public class AwareBeanService implements BeanNameAware, BeanFactoryAware, BeanClassLoaderAware, InitializingBean {
    String beanName;
    BeanFactory beanFactory;
    ClassLoader classLoader;

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader=classLoader;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory=beanFactory;
    }

    @Override
    public void setBeanName(String beanName) {
        this.beanName=beanName;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet");
    }
}
