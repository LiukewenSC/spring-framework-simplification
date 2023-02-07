package com.kewen.spring.beans.factory;

import com.kewen.spring.beans.exception.BeansException;
import com.kewen.spring.beans.factory.config.BeanDefinition;
import com.kewen.spring.beans.factory.config.BeanPostProcessor;
import com.kewen.spring.beans.factory.support.AbstractBeanFactory;
import com.kewen.spring.beans.factory.support.BeanDefinitionRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @descrpition 默认的可配置工厂
 * @author kewen
 * @since 2023-02-06 14:01
 */
public class DefaultListableBeanFactory extends AbstractBeanFactory implements ConfigurableListableBeanFactory, BeanDefinitionRegistry {




    public DefaultListableBeanFactory(BeanFactory parent) {
        setParentBeanFactory(parent);
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


    /** Map of bean definition objects, keyed by bean name. */
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);	/** List of bean definition names, in registration order. */
    private volatile List<String> beanDefinitionNames = new ArrayList<>(256);

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        if (beanDefinitionMap.containsKey(beanName)) {
            throw new BeansException(" beanDefinition is exists");
        }
        this.beanDefinitionMap.put(beanName,beanDefinition);
        this.beanDefinitionNames.add(beanName);
    }

    @Override
    public void registerAlias(String beanName, String[] alias) {
        //todo ，还没看明白
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public int getBeanDefinitionCount() {
        return beanDefinitionMap.size();
    }

    @Override
    public String[] getBeanDefinitionNames() {
        return beanDefinitionMap.keySet().toArray(new String[0]);
    }

    @Override
    public String[] getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
        return new String[0];
    }

    @Override
    public void freezeConfiguration() {

    }

    @Override
    public void preInstantiateSingletons() throws BeansException {
        List<String> definitionNames = beanDefinitionNames;
        for (String beanName : definitionNames) {
            //此处原框架需要区分是FactoryBean还是普通的单例bean
            getBean(beanName);
        }

        for (String beanName : definitionNames) {
            Object singleton = getSingleton(beanName);
            //初始化完成后再执行对应后处理
            if (singleton instanceof SmartInitializingSingleton){
                ((SmartInitializingSingleton) singleton).afterSingletonsInstantiated();
            }
        }


    }
}
