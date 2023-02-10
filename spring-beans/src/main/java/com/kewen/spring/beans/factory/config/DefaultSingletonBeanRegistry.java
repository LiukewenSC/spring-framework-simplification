package com.kewen.spring.beans.factory.config;

import com.kewen.spring.beans.factory.ObjectFactory;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.core.util.Assert;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @descrpition 默认的单例注册容器
 * @author kewen
 * @since 2023-02-07 16:23
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    /** Maximum number of suppressed exceptions to preserve. */
    private static final int SUPPRESSED_EXCEPTIONS_LIMIT = 100;


    /**
     * 一级缓存，存单例实例
     */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
    /**
     * 二级缓存，存未完全初始化的实例
     */
    private final Map<String, Object> earlySingletonObjects = new ConcurrentHashMap<>(16);

    /**
     * 存 ObjectFactory实例
     */
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);

    private final Set<String> registeredSingletons = new LinkedHashSet<>(256);

    private final Set<String> singletonsCurrentlyInCreation =
            Collections.newSetFromMap(new ConcurrentHashMap<>(16));

    private final Set<String> inCreationCheckExclusions =
            Collections.newSetFromMap(new ConcurrentHashMap<>(16));

    @Nullable
    private Set<Exception> suppressedExceptions;

    private boolean singletonsCurrentlyInDestruction = false;

    /** Disposable bean instances: bean name to disposable instance. */
    private final Map<String, Object> disposableBeans = new LinkedHashMap<>();

    /** Map between containing bean names: bean name to Set of bean names that the bean contains. */
    private final Map<String, Set<String>> containedBeanMap = new ConcurrentHashMap<>(16);

    /** Map between dependent bean names: bean name to Set of dependent bean names. */
    private final Map<String, Set<String>> dependentBeanMap = new ConcurrentHashMap<>(64);

    /** Map between depending bean names: bean name to Set of bean names for the bean's dependencies. */
    private final Map<String, Set<String>> dependenciesForBeanMap = new ConcurrentHashMap<>(64);


    public boolean isSingletonCurrentlyInCreation(String beanName) {
        return this.singletonsCurrentlyInCreation.contains(beanName);
    }

    @Override
    public void registerSingleton(String beanName, Object singletonObject) {
        singletonObjects.put(beanName,singletonObject);

    }

    @Override
    public Object getSingleton(String beanName) {
        return getSingleton(beanName, true);
    }


    /**
     * 添加到一级缓存中，从二级缓存移除
     * @param beanName
     * @param singletonObject
     */
    protected void addSingleton(String beanName, Object singletonObject) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.put(beanName, singletonObject);
            this.singletonFactories.remove(beanName);
            this.earlySingletonObjects.remove(beanName);
            this.registeredSingletons.add(beanName);
        }
    }

    /**
     * 添加工厂方法，为 三级缓存 singletonFactories 设置值
     * @param beanName
     * @param singletonFactory
     */
    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
        Assert.notNull(singletonFactory, "Singleton factory must not be null");
        synchronized (this.singletonObjects) {
            if (!this.singletonObjects.containsKey(beanName)) {
                this.singletonFactories.put(beanName, singletonFactory);
                this.earlySingletonObjects.remove(beanName);
                this.registeredSingletons.add(beanName);
            }
        }
    }

    /**
     * 此为解决三级缓存很重要的一个方法，
     * @param beanName bean的名称
     * @param allowEarlyReference 是否允许加载早期bean
     * @return
     */
    @Nullable
    protected Object getSingleton(String beanName, boolean allowEarlyReference) {

        Object singletonObject = singletonObjects.get(beanName);
        if (singletonObject ==null){
            singletonObject = earlySingletonObjects.get(beanName);
            if (singletonObject == null && allowEarlyReference){
                //原框架此处加双重检测锁执行的，此处就简化不加了
                ObjectFactory<?> objectFactory = singletonFactories.get(beanName);
                if (objectFactory != null){
                    //从三级缓存中拿到之后加入到二级缓存，此处是解决循环依赖的一环，从三级中放至二级，除了B->A之外，C->A就直接从二级缓存中拿
                    singletonObject = objectFactory.getObject();
                    earlySingletonObjects.put(beanName,singletonObject);
                    singletonFactories.remove(beanName);
                }
            }
        }
        return singletonObject;
    }

    /**
     * 从ObjectFactory工厂中获取bean实例，
     *  singletonFactory的实现中可以创建bean的逻辑，
     *  此处获取到工厂中的bean实例后会加入到 一级缓存singletonObjects中，然后从二级缓存中移除
     * @param beanName
     * @param singletonFactory
     * @return
     */
    public Object getSingleton(String beanName, ObjectFactory<?> singletonFactory) {
        Object singletonObject = singletonObjects.get(beanName);
        if (singletonObject ==null){
            //这儿就会走创建bean的流程
            singletonObject= singletonFactory.getObject();
            addSingleton(beanName,singletonObject);
        }
        return singletonObject;
    }
    @Override
    public boolean containsSingleton(String beanName) {
        return singletonObjects.containsKey(beanName);
    }

    @Override
    public String[] getSingletonNames() {
        Set<String> strings = singletonObjects.keySet();
        return strings.toArray(new String[0]);
    }

    @Override
    public int getSingletonCount() {
        return singletonObjects.size();
    }


}
