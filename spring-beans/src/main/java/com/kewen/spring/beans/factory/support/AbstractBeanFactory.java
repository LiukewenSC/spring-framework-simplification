package com.kewen.spring.beans.factory.support;

import com.kewen.spring.beans.factory.BeanFactory;
import com.kewen.spring.beans.factory.ConfigurableBeanFactory;
import com.kewen.spring.beans.factory.ConfigurableListableBeanFactory;
import com.kewen.spring.beans.factory.config.BeanPostProcessor;
import com.kewen.spring.beans.factory.config.DefaultSingletonBeanRegistry;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @descrpition 抽象的beanfactory
 * @author kewen
 * @since 2023-02-07 10:10
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {

    protected BeanFactory parentBeanFactory;

    private final List<BeanPostProcessor> beanPostProcessors = new CopyOnWriteArrayList<>();
    public BeanFactory getParentBeanFactory() {
        return parentBeanFactory;
    }

    public void setParentBeanFactory(BeanFactory parentBeanFactory) {
        this.parentBeanFactory = parentBeanFactory;
    }

    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        beanPostProcessors.remove(beanPostProcessor);
        beanPostProcessors.add(beanPostProcessor);
    }


    @Override
    public Object getBean(String beanName) {
        return null;
    }

    @Override
    public <T> T getBean(String beanName, Class<T> clazz) {
        return null;
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        return null;
    }

    @Override
    public boolean containsBean(String beanName) {
        return false;
    }
}
