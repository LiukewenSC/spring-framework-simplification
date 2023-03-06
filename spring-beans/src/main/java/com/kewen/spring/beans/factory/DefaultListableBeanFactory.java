package com.kewen.spring.beans.factory;

import com.kewen.spring.beans.*;
import com.kewen.spring.beans.exception.BeanDefinitionException;
import com.kewen.spring.beans.exception.BeansException;
import com.kewen.spring.beans.factory.config.*;
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
public class DefaultListableBeanFactory extends AbstractBeanFactory implements ConfigurableListableBeanFactory, BeanDefinitionRegistry {


    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
    /**
     * List of bean definition names, in registration order.
     */
    private volatile List<String> beanDefinitionNames = new ArrayList<>(256);

    private boolean allowCircularReferences = true;

    public DefaultListableBeanFactory(BeanFactory parent) {
        setParentBeanFactory(parent);
    }

    @Override
    protected BeanDefinition getBeanDefinition(String beanName) throws BeanDefinitionException {
       return beanDefinitionMap.get(beanName);
    }

    @Override
    protected Object createBean(String beanName, RootBeanDefinition mbd, Object[] args) throws BeansException {

        //此处有获取Class逻辑，并有SecurityManager相关的逻辑，简化了，直接复制一次就好了
        // 原来框架专门有说要复制一次，不使用mergeRootBeanDefinition
        RootBeanDefinition mbdToUse = new RootBeanDefinition(mbd);

        //此处可以创建代理，可能返回代理类
        Object bean = resolveBeforeInstantiation(beanName, mbdToUse);

        if (bean != null) {
            return bean;
        }

        Object beanInstance = doCreateBean(beanName, mbdToUse, args);

        //创建完成
        return beanInstance;
    }

    private Object  doCreateBean(String beanName, RootBeanDefinition mbdToUse, Object[] args) {

        //此处有从 this.factoryBeanInstanceCache.remove(beanName) 取回factoryBean的操作
        BeanWrapper beanWrapper = createBeanInstance(beanName, mbdToUse, args);

        Object bean = beanWrapper.getWrappedInstance();
        Class<?> beanClass = beanWrapper.getWrappedClass();

        //此处有处理Merge的一个扩展，便于在实例化后对bean做最后处理 applyMergedBeanDefinitionPostProcessors()

        //此处解决循环依赖（貌似切面也在这里处理），在允许循环依赖且为单例且 在循环中正在创建
        boolean earlySingletonExposure = mbdToUse.isSingleton() && this.allowCircularReferences && isSingletonCurrentlyInCreation(beanName);
        if (earlySingletonExposure) {
            //如果一级缓存中没有，则将 ()-> 创建的匿名对象加入到三级缓存中，
            // 例如A->B->A循环而言，现在创建A，加入到三级缓存中，在 populateBean的时候会加载B，从而创建B,
            //      再当B调用A的时候通过getSingleton()方法会拿到三级缓存中的工厂，通过getObject()拿到实例，同时加入二级缓存中
            // 此流程则可以解决循环依赖问题，此处不直接放二级缓存原因是将实例化延后，可以继续增加切面，动态代理等，因为加入后的对象就不能再变了
            addSingletonFactory(beanName, () -> getEarlyBeanReference(beanName, mbdToUse, bean));
        }

        Object exposedObject = bean;

        //注入属性
        populateBean(beanName, mbdToUse, beanWrapper);

        //初始化后的 bean的各种钩子函数 包括各种Aware和Inintionbean
        exposedObject = initializeBean(beanName, exposedObject, mbdToUse);


        if (earlySingletonExposure) {
            //从缓存中获取单例值
            Object earlySingletonReference = getSingleton(beanName, false);
            //判断是否被改变了(是否有过增强)没有则返回缓存中的，有则返回代理的
            if (earlySingletonReference != null && exposedObject == bean) {
                exposedObject = earlySingletonReference;
            }
        }
        //注册单例模式的注销方法
        registerDisposableBeanIfNecessary(beanName, exposedObject, mbdToUse);
        return exposedObject;
    }

    protected void populateBean(String beanName, RootBeanDefinition mbd, @Nullable BeanWrapper bw) {

        PropertyValues pvs = mbd.getPropertyValues();

        //此处有定义bean执行bean注入之前的钩子函数，暂时不处理
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                boolean isPost = ((InstantiationAwareBeanPostProcessor) beanPostProcessor)
                        .postProcessAfterInstantiation(bw.getWrappedInstance(), beanName);
                if (!isPost) {
                    //返回了false就结束注入
                    return;
                }
            }
        }


        int resolvedAutowireMode = mbd.getResolvedAutowireMode();
        if (resolvedAutowireMode == AutowireCapableBeanFactory.AUTOWIRE_BY_NAME) {
            //按照名称注入
            autowireByName(beanName, pvs, bw);
        } else if (resolvedAutowireMode == AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE) {
            //按照类型注入

        }
        // 按照property配置注入

        PropertyDescriptor[] filteredPds = null;
        if (pvs == null) {
            pvs = new MutablePropertyValues();
        }
        for (BeanPostProcessor bp : getBeanPostProcessors()) {
            if (bp instanceof InstantiationAwareBeanPostProcessor) {
                InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
                //获取bean定义的相关东西， @Autowired 主要就是在这里处理了， 其实现类为
                PropertyValues pvsToUse = ibp.postProcessProperties(pvs, bw.getWrappedInstance(), beanName);
                if (pvsToUse == null) {
                    if (filteredPds == null) {
                        //未找到默认提供全部的方法
                        filteredPds = filterPropertyDescriptorsForDependencyCheck(bw);
                    }
                    pvsToUse = ibp.postProcessPropertyValues(pvs, filteredPds, bw.getWrappedInstance(), beanName);
                    if (pvsToUse == null) {
                        return;
                    }
                }
                pvs = pvsToUse;
            }
        }
    }

    protected PropertyDescriptor[] filterPropertyDescriptorsForDependencyCheck(BeanWrapper bw) {
        // 此处排除一些方法，框架说主要是 cglib 相关的。这里先不考虑
        return bw.getPropertyDescriptors();
    }

    /**
     * 按照名称注入，原来之前都理解错了意思，按名称注入是按照字段的名称和set方法去找对应的bean，然后注册容器中已经有了的bean
     * 所以说实际上xml中配置了autowire="byName" 属性之后就不用再配置其他东西了，这才是按名称注入
     * 一般我们写的<property>标签都是自己装配的注入方式，需要按照既定的目标解析的
     * 注意，按名称注入查找的对应的set方法，因此字段对应的set方法去掉set之后必须和原来的名字保持一致，否则不会注入
     *
     * @param beanName
     * @param pvs
     * @param bw
     */
    private void autowireByName(String beanName, PropertyValues pvs, BeanWrapper bw) {


        //解析属性注入的Bean的名字
        Collection<String> propertyNames = unsatisfiedNonSimpleProperties(pvs, bw.getPropertyDescriptors());

        for (String propertyName : propertyNames) {
            Object bean = getBean(propertyName);

            //替换得到的bean，以便于注入
            //propertyValues.add(propertyName,bean);


            Object wrappedInstance = bw.getWrappedInstance();

            //直接注入，放弃原框架在外层的大量逻辑，此处简化了很多，但目的是把属性注入到bean中
            PropertyDescriptor descriptor = bw.getPropertyDescriptor(propertyName);
            Method writeMethod = descriptor.getWriteMethod();
            try {
                writeMethod.invoke(wrappedInstance, bean);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


    }

    /**
     * 解析属性注入的Bean名字
     *
     * @param pvs 配置的定义的值
     * @param pds 保存原类中定义的
     * @return
     */
    protected Collection<String> unsatisfiedNonSimpleProperties(PropertyValues pvs, PropertyDescriptor[] pds) {
        Set<String> result = new TreeSet<>();
        for (PropertyDescriptor pd : pds) {
            if (pvs.contains(pd.getName())) {
                result.add(pd.getName());
            }
        }
        return result;
    }

    protected Object initializeBean(String beanName, Object bean, @Nullable RootBeanDefinition mbd) {
        //todo bean的各种钩子函数

        return bean;
    }

    /**
     * 获取一个引用，以便尽早访问指定的bean，通常是为了解决循环引用。
     *
     * @param beanName
     * @param mbd
     * @param bean
     * @return
     */
    protected Object getEarlyBeanReference(String beanName, RootBeanDefinition mbd, Object bean) {
        Object exposedObject = bean;
        for (BeanPostProcessor bp : getBeanPostProcessors()) {
            if (bp instanceof SmartInstantiationAwareBeanPostProcessor) {
                SmartInstantiationAwareBeanPostProcessor ibp = (SmartInstantiationAwareBeanPostProcessor) bp;
                exposedObject = ibp.getEarlyBeanReference(exposedObject, beanName);
            }
        }
        return exposedObject;
    }


    /**
     * 创建beanWrapper
     */
    private BeanWrapper createBeanInstance(String beanName, RootBeanDefinition mbdToUse, Object[] args) {
        //创建Bean的包装简化处理，原框架还有其他需要实例化bean的情况，如Class不存在，暂时不讨论
        Object instance = BeanUtils.instantiateClass(this, mbdToUse.getBeanClass());
        return new BeanWrapperImpl(instance);
    }

    /**
     * 此处有逻辑给bean一个机会返回代理类而不是原类
     *
     * @param beanName
     * @param mbd
     * @return
     */
    protected Object resolveBeforeInstantiation(String beanName, RootBeanDefinition mbd) {
        Object bean = null;

        // 此处有逻辑给bean一个机会返回代理类而不是原类
        Class<?> beanClass = mbd.getBeanClass();
        bean = applyBeanPostProcessorsBeforeInstantiation(beanName, beanClass);
        if (bean != null) {
            bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        }
        return bean;
    }

    /**
     * 处理
     *
     * @param beanClass
     * @param beanName
     * @return
     */
    protected Object applyBeanPostProcessorsBeforeInstantiation(String beanName, Class<?> beanClass) {
        for (BeanPostProcessor bp : getBeanPostProcessors()) {
            if (bp instanceof InstantiationAwareBeanPostProcessor) {
                InstantiationAwareBeanPostProcessor ibp = (InstantiationAwareBeanPostProcessor) bp;
                Object result = ibp.postProcessBeforeInstantiation(beanClass, beanName);
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    /**
     * @param existingBean
     * @param beanName
     * @return
     * @throws BeansException
     */
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
            throws BeansException {

        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessAfterInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
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
