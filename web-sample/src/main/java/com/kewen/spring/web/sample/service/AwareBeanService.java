package com.kewen.spring.web.sample.service;

import com.kewen.spring.beans.exception.BeansException;
import com.kewen.spring.beans.factory.BeanClassLoaderAware;
import com.kewen.spring.beans.factory.BeanFactory;
import com.kewen.spring.beans.factory.BeanFactoryAware;
import com.kewen.spring.beans.factory.BeanNameAware;
import com.kewen.spring.beans.factory.InitializingBean;
import com.kewen.spring.context.ApplicationContext;
import com.kewen.spring.context.ApplicationContextAware;
import com.kewen.spring.context.EnvironmentAware;
import com.kewen.spring.context.annotation.Service;
import com.kewen.spring.core.env.Environment;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-07
 */
@Service
public class AwareBeanService implements BeanNameAware, BeanFactoryAware, BeanClassLoaderAware, InitializingBean, ApplicationContextAware, EnvironmentAware {
    String beanName;
    BeanFactory beanFactory;
    ClassLoader classLoader;
    ApplicationContext applicationContext;
    Environment environment;

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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
