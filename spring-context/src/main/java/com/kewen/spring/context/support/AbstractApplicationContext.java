package com.kewen.spring.context.support;

import com.kewen.spring.beans.exception.BeansException;
import com.kewen.spring.beans.factory.ConfigurableListableBeanFactory;
import com.kewen.spring.beans.factory.DefaultListableBeanFactory;
import com.kewen.spring.beans.factory.config.BeanFactoryPostProcessor;
import com.kewen.spring.context.ApplicationContext;
import com.kewen.spring.context.ApplicationEvent;
import com.kewen.spring.context.ApplicationListener;
import com.kewen.spring.context.ConfigurableApplicationContext;
import com.kewen.spring.context.event.ApplicationEventMulticaster;
import com.kewen.spring.context.event.ApplicationEventPublisher;
import com.kewen.spring.context.event.ContextRefreshedEvent;
import com.kewen.spring.context.event.PayloadApplicationEvent;
import com.kewen.spring.context.event.SimpleApplicationEventMulticaster;
import com.kewen.spring.core.ConfigurableEnvironment;
import com.kewen.spring.core.io.ClassPathResource;
import com.kewen.spring.core.io.Resource;
import com.kewen.spring.core.io.ResourceLoader;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.core.util.Assert;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * @descrpition  上下文抽象类
 *         此类完成大部分applicationContext逻辑，特别重要的类
 * @author kewen
 * @since 2023-02-07
 */
public abstract class AbstractApplicationContext implements ConfigurableApplicationContext, ResourceLoader, ApplicationEventPublisher {
    private final List<BeanFactoryPostProcessor> beanFactoryPostProcessors = new ArrayList<>();


    private final Object startupShutdownMonitor = new Object();

    protected String id;


    protected String configLocation;
    protected String[] configLocations;

    protected ApplicationContext parent;
    protected ConfigurableEnvironment environment;	/** Bean factory for this context. */

    @Nullable
    protected volatile DefaultListableBeanFactory beanFactory;
    /** Statically specified listeners. */
    private final Set<ApplicationListener<?>> applicationListeners = new LinkedHashSet<>();
    @Nullable
    private ApplicationEventMulticaster applicationEventMulticaster;
    @Nullable
    private Set<ApplicationEvent> earlyApplicationEvents = new HashSet<>();

    /**
     * 在BeanDefinitionReader中解析配置文件路径需要用到
     */
    protected ClassLoader classLoader;


    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setConfigLocation(String configLocation) {
        //this.configLocation=configLocation;
        if (configLocation !=null){
            setConfigLocations(configLocation.split(","));
        }
    }

    public void setConfigLocations(String[] locations) {
        this.configLocations = locations;
    }

    public String[] getConfigLocations() {
        return configLocations;
    }

    @Override
    public void setEnvironment(ConfigurableEnvironment environment) {
        this.environment=environment;
    }

    @Override
    public ConfigurableEnvironment getEnvironment() {
        return environment;
    }

    @Override
    public void setParent(ApplicationContext parent) {
        this.parent=parent;
    }

    public ApplicationEventMulticaster getApplicationEventMulticaster() {
        return applicationEventMulticaster;
    }

    public Set<ApplicationListener<?>> getApplicationListeners() {
        return applicationListeners;
    }

    @Override
    public void refresh() {

        synchronized (startupShutdownMonitor) {

            try {
                prepareRefresh();

                //主要创建BeanFactory和注册BeanDefinition
                ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

                //主要添加各种bean处理器
                prepareBeanFactory(beanFactory);

                //添加servlet处理器，注册域scope和环境bean
                postProcessBeanFactory(beanFactory);

                //处理BeanFactoryPostProcessor和BeanDefinitionRegisterPostProcessor
                invokeBeanFactoryPostProcessors(beanFactory);

                //注册
                registerBeanPostProcessors(beanFactory);

                //初始化消息源
                initMessageSource();

                // 初始化事件控制器
                initApplicationEventMulticaster(beanFactory);

                //刷新特殊的类，先不管
                onRefresh();

                //注册监听器
                registerListeners();

                //完成bean工厂初始化 ，此处初始化单例bean
                finishBeanFactoryInitialization(beanFactory);

                //完成初始化
                finishRefresh();
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    private void initApplicationEventMulticaster(ConfigurableListableBeanFactory beanFactory) {


        this.applicationEventMulticaster = new SimpleApplicationEventMulticaster();

        beanFactory.registerSingleton("applicationEventMulticaster",this.applicationEventMulticaster);


    }

    private void initMessageSource() {
        //先不管，把主要流程走了
    }

    @Override
    public void setId(String id) {
        this.id=id;
    }

    private void finishRefresh() {
        // 清除资源缓存
        //clearResourceCaches();

        // 初始化生命周期处理器
        // initLifecycleProcessor();

        //  执行生命周期处理器完成方法
        // getLifecycleProcessor().onRefresh();

        // 发布上下文初始化完成事件
        publishEvent(new ContextRefreshedEvent(this));

        // 安全相关吧，注册到哪里
        // LiveBeansView.registerApplicationContext(this);
    }

    @Override
    public void publishEvent(Object event) {
        ApplicationEvent applicationEvent;
        if (event instanceof ApplicationEvent){
            applicationEvent = ((ApplicationEvent) event);
        } else {
            //包装一个载荷事件
            applicationEvent = new PayloadApplicationEvent<>(this,event);
        }
        applicationEventMulticaster.multicastEvent(applicationEvent);

        if (this.parent != null){
            parent.publishEvent(event);
        }
    }

    private void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {

        //注册 ConversionService 转换器


        //注册 LoadTimeWeaverAware


        //冻结bean定义， 及不再注册beanDefinition
        beanFactory.freezeConfiguration();


        //初始化单例bean
        beanFactory.preInstantiateSingletons();

    }

    private void registerListeners() {

        //注册监听器
        for (ApplicationListener<?> listener : getApplicationListeners()) {
            applicationEventMulticaster.addApplicationListener(listener);
        }

        //注册容器中的监听器
        List<String> listenerNames = beanFactory.getBeanNamesForType(ApplicationListener.class, true, false);
        if (listenerNames !=null){
            for (String listenerName : listenerNames) {
                applicationEventMulticaster.addApplicationListenerBean(listenerName);
            }
        }

        //注册事件
        for (ApplicationEvent earlyApplicationEvent : earlyApplicationEvents) {
            applicationEventMulticaster.multicastEvent(earlyApplicationEvent);
        }

    }

    protected void onRefresh() throws BeansException {
        // For subclasses: do nothing by default.
    }

    private void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
        PostProcessorRegistrationDelegate.registerBeanPostProcessors(beanFactory, this);
    }

    private void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {

        //处理beanDefinitionRegister的相关，包括 BeanDefinitionRegisterPostProcessor
        PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors());

        //还有两个LoadTimeWeaverAwareProcessor 和 ContextTypeMatchClassLoader 的处理，先不管

    }

    protected abstract void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory);

    private void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {

        //添加上下文处理器， 主要用来处理 ApplicationContextAware
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

        //todo 添加上下文监听器，做啥来着
        beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(this));

    }

    private ConfigurableListableBeanFactory obtainFreshBeanFactory() {
        refreshBeanFactory();
        return getBeanFactory();
    }

    private void refreshBeanFactory() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory(parent);
        try {
            loadBeanDefinitions(beanFactory);
            this.beanFactory=beanFactory;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    public DefaultListableBeanFactory getBeanFactory(){
        return this.beanFactory;
    }

    /**
     * 加载BeanDefinition，这个很重要
     * @param beanFactory
     */
    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws IOException ;


    private void prepareRefresh() {


    }


    @Override
    public String getApplicationName() {
        return null;
    }

    @Override
    public ApplicationContext getParent() {
        return null;
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


    @Override
    public Resource getResource(String location) {
        return new ClassPathResource(location,classLoader);
    }


    @Override
    public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor) {
        Assert.notNull(postProcessor, "BeanFactoryPostProcessor must not be null");
        this.beanFactoryPostProcessors.add(postProcessor);
    }

    public List<BeanFactoryPostProcessor> getBeanFactoryPostProcessors() {
        return this.beanFactoryPostProcessors;
    }

}
