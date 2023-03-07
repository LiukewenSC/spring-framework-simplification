package com.kewen.spring.beans.factory;

import com.kewen.spring.beans.*;
import com.kewen.spring.beans.exception.BeanDefinitionException;
import com.kewen.spring.beans.exception.BeansException;
import com.kewen.spring.beans.factory.config.*;
import com.kewen.spring.beans.factory.support.AbstractAutowireCapableBeanFactory;
import com.kewen.spring.beans.factory.support.AbstractBeanFactory;
import com.kewen.spring.beans.factory.support.BeanDefinitionRegistry;
import com.kewen.spring.core.lang.Nullable;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author kewen
 * @descrpition 默认的可配置工厂
 * 继承AbstractBeanFactory，同时实现了ConfigurableListableBeanFactory，BeanDefinitionRegistry
 * 最底层的实现类，维护了 beanDefinitionMap ，因此，得以实现创建bean的全过程，
 * 实现了ListableBeanFactory，因此也有获取bean定义的方法
 * 实现了BeanDefinitionRegistry，因此也有注册bean的方法
 * @since 2023-02-06
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory implements ConfigurableListableBeanFactory, BeanDefinitionRegistry {


    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
    /**
     * List of bean definition names, in registration order.
     */
    private volatile List<String> beanDefinitionNames = new ArrayList<>(256);



    public DefaultListableBeanFactory(BeanFactory parent) {
        setParentBeanFactory(parent);
    }

    @Override
    protected BeanDefinition getBeanDefinition(String beanName) throws BeanDefinitionException {
       return beanDefinitionMap.get(beanName);
    }



    /**
     * Map of bean definition objects, keyed by bean name.
     */

    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        if (beanDefinitionMap.containsKey(beanName)) {
            throw new BeansException(" beanDefinition is exists");
        }
        this.beanDefinitionMap.put(beanName, beanDefinition);
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
    public List<String> getBeanNamesForType(Class<?> type, boolean includeNonSingletons, boolean allowEagerInit) {
        List<String> beanNames = new ArrayList<>();
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            BeanDefinition definition = entry.getValue();
            if (type.isAssignableFrom(definition.getBeanClass())) {
                beanNames.add(beanName);
            }
        }
        // TODO: 2023/3/6 此处需要加入手动注入的bean，手动注入的bean不在beanDefinition中，但是也能够加入bean中
        return beanNames;
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type, boolean includeNonSingletons, boolean allowEagerInit) throws BeansException {
        HashMap<String, T> map = new HashMap<>();
        List<String> beanNamesForType = getBeanNamesForType(type, includeNonSingletons, allowEagerInit);
        for (String beanName : beanNamesForType) {
            T bean = getBean(beanName);
            map.put(beanName,bean);
        }
        return map;
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
            Object singleton = getBean(beanName);
            //初始化完成后再执行对应后处理
            if (singleton instanceof SmartInitializingSingleton) {
                ((SmartInitializingSingleton) singleton).afterSingletonsInstantiated();
            }
        }


    }

    @Override
    public <T> T getBean(String beanName, Class<T> clazz) {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        Class<?> beanClass = beanDefinition.getBeanClass();
        if (!clazz.isAssignableFrom(beanClass)) {
            throw new BeansException("getBean error");
        }
        return getBean(beanName);
    }

    @Override
    public <T> T getBean(Class<T> clazz) {
        List<String> beanNames = getBeanNamesForType(clazz, true, true);
        if (beanNames.size() == 1) {
            return getBean(beanNames.get(0));
        } else if (beanNames.size() > 1) {
            for (String beanName : beanNames) {
                BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
                if (beanDefinition.isPrimary()) {
                    return getBean(beanName);
                }
            }
        }
        throw new BeansException("工厂里没有此bean : "+clazz.getName());
    }

}
