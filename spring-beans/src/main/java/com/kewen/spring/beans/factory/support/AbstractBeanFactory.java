package com.kewen.spring.beans.factory.support;

import com.kewen.spring.beans.BeanUtils;
import com.kewen.spring.beans.exception.BeanDefinitionException;
import com.kewen.spring.beans.exception.BeansException;
import com.kewen.spring.beans.factory.BeanFactory;
import com.kewen.spring.beans.factory.ConfigurableBeanFactory;
import com.kewen.spring.beans.factory.ConfigurableListableBeanFactory;
import com.kewen.spring.beans.factory.ObjectFactory;
import com.kewen.spring.beans.factory.config.BeanDefinition;
import com.kewen.spring.beans.factory.config.BeanPostProcessor;
import com.kewen.spring.beans.factory.config.DefaultSingletonBeanRegistry;
import com.kewen.spring.beans.factory.config.RootBeanDefinition;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.core.util.ClassUtils;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @descrpition 抽象的beanfactory
 *      实现了大多数的bean的获取方法，但是没有提供bean的创建流程，只有一个抽象的方法供实现
 * @author kewen
 * @since 2023-02-07
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry implements ConfigurableBeanFactory {

    protected BeanFactory parentBeanFactory;

    private final List<BeanPostProcessor> beanPostProcessors = new CopyOnWriteArrayList<>();

    private final Map<String, RootBeanDefinition> mergedBeanDefinitions = new ConcurrentHashMap<>(256);


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

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return beanPostProcessors;
    }

    @Override
    public <T> T  getBean(String beanName) {
        return doGetBean(beanName,null,null,false);
    }

    protected Object getObjectForBeanInstance(Object beanInstance, String name, String beanName, @Nullable RootBeanDefinition mbd) {
        //此处处理beanFactory的情况，是beanFactory会报错 ，先暂时忽略
        return beanInstance;
    }

    private String transformName(String beanName) {
        //todo 有特殊符号之类的...
        return beanName;
    }
    protected RootBeanDefinition getMergedBeanDefinition(String beanName, BeanDefinition bd)
            throws BeanDefinitionException {

        RootBeanDefinition rootBeanDefinition = mergedBeanDefinitions.get(beanName);
        if (rootBeanDefinition !=null){
            return rootBeanDefinition;
        }
        rootBeanDefinition = new RootBeanDefinition(bd);

        mergedBeanDefinitions.put(beanName,rootBeanDefinition);

        return rootBeanDefinition;
    }

    /**
     * 获取bean，返回可能为空
     * @param name
     * @param requiredType 暂时没用
     * @param args 暂时没用
     * @param typeCheckOnly 暂时没用
     * @param <T>
     * @return
     * @throws BeansException
     */
    @Nullable
    protected <T> T doGetBean(String name, @Nullable Class<T> requiredType, @Nullable Object[] args, boolean typeCheckOnly) throws BeansException {

        //解析名字，可能有符号&之类的，先不处理那种复杂的情况
        String beanName = transformName(name);

        Object sharedInstance = getSingleton(beanName);
        if (sharedInstance !=null){
            return (T)sharedInstance;
        }

        //在父级beanFactory中查找
        BeanFactory parent = getParentBeanFactory();
        if (parent !=null){
            sharedInstance = parent.getBean(beanName);
        }
        if (sharedInstance != null){
            return (T)sharedInstance;
        }

        //简单处理，后面要用到BeanDefinition 如果没有获取到的话就直接返回空了
        BeanDefinition beanDefinition = getBeanDefinition(beanName);
        if (beanDefinition==null){
            return null;
        }
        RootBeanDefinition rootBeanDefinition = getMergedBeanDefinition(beanName, beanDefinition);

        //这儿就会去先初始化依赖的bean，就是配置的在什么bean之后初始化的，，
        // 注意：非注入的bean
        String[] dependsOns = rootBeanDefinition.getDependsOn();
        if (dependsOns != null){
            for (String dependsOn : dependsOns) {
                getBean(dependsOn);
            }
        }


        //此处可以不这么麻烦的一个理解，可以直接理解为从一级缓存中获取，没有获取到就创建
        sharedInstance = getSingleton(beanName, new ObjectFactory<Object>() {
            @Override
            public Object getObject() throws BeansException {
                return createBean(beanName, rootBeanDefinition, null);
            }
        });

        //处理 beanFactory ，是beanFactory会报错
        Object bean = getObjectForBeanInstance(sharedInstance, name, beanName, rootBeanDefinition);


        //如果为原型模式，则else 分支会创建原型模式的bean并返回


        //如果传入转换类型，还要对bean进行转换

        return (T)bean;


    }
    @Override
    public boolean containsBean(String beanName) {
        return getBean(beanName) != null;
    }

    protected abstract BeanDefinition getBeanDefinition(String beanName) throws BeanDefinitionException;

    protected abstract Object createBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args)
            throws BeansException;

    /**
     * 注册注销方法，仅用于单例模式
     * @param beanName
     * @param bean
     * @param mbd
     */
    protected void registerDisposableBeanIfNecessary(String beanName, Object bean, RootBeanDefinition mbd) {

    }

}
