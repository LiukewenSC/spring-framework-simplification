package com.kewen.spring.web.context;

import com.kewen.spring.beans.factory.ConfigurableListableBeanFactory;
import com.kewen.spring.beans.factory.DefaultListableBeanFactory;
import com.kewen.spring.beans.factory.xml.XmlBeanDefinitionReader;
import com.kewen.spring.context.ApplicationContext;
import com.kewen.spring.core.ConfigurableEnvironment;
import com.kewen.spring.core.io.ClassPathResource;
import com.kewen.spring.core.io.Resource;
import com.kewen.spring.core.io.ResourceLoader;

import javax.servlet.ServletContext;
import java.io.IOException;

public class XmlWebApplicationContext implements WebApplicationContext, ResourceLoader {

    private final Object startupShutdownMonitor = new Object();

    private String id;

    private ServletContext servletContext;

    private String configLocation;
    private String[] configLocations;

    private ApplicationContext parent;
    private ConfigurableEnvironment environment;

    /**
     * 在BeanDefinitionReader中解析配置文件路径需要用到
     */
    private ClassLoader classLoader;
    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
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

    @Override
    public void refresh() {

        synchronized (startupShutdownMonitor) {

            try {
                prepareRefresh();

                ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

                prepareBeanFactory(beanFactory);

                postProcessBeanFactory(beanFactory);

                invokeBeanFactoryPostProcessors(beanFactory);

                registerBeanPostProcessors(beanFactory);

                onRefresh();

                registerListeners();

                finishBeanFactoryInitialization(beanFactory);

                finishRefresh();
            } catch (Throwable e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void setId(String id) {
        this.id=id;
    }

    private void finishRefresh() {

    }

    private void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {


    }

    private void registerListeners() {

    }

    private void onRefresh() {


    }

    private void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {

    }

    private void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {

    }

    private void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {

    }

    private void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {

    }

    private ConfigurableListableBeanFactory obtainFreshBeanFactory() {
        refreshBeanFactory();
        return getBeanFactory();
    }

    private void refreshBeanFactory() {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory(parent);
        try {
            loadBeanDefinitions(beanFactory);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 加载BeanDefinition，这个很重要
     * @param beanFactory
     */
    private void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws IOException {
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);

        beanDefinitionReader.setEnvironment(getEnvironment());
        beanDefinitionReader.setResourceLoader(this);


        initBeanDefinitionReader(beanDefinitionReader);

        loadBeanDefinitions(beanDefinitionReader);
    }
    protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws IOException {
        String[] configLocations = getConfigLocations();
        if (configLocations != null) {
            for (String configLocation : configLocations) {
                reader.loadBeanDefinitions(configLocation);
            }
        }
    }

    private void initBeanDefinitionReader(XmlBeanDefinitionReader beanDefinitionReader) {
        // 原来的框架中就什么也没有，可以作为一个扩展点
    }

    private ConfigurableListableBeanFactory getBeanFactory() {
        return null;
    }


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
}
