package com.kewen.spring.web.context;

import com.kewen.spring.beans.factory.ConfigurableListableBeanFactory;
import com.kewen.spring.context.ApplicationContext;
import com.kewen.spring.core.ConfigurableEnvironment;

import javax.servlet.ServletContext;

public class XmlWebApplicationContext implements WebApplicationContext {

    private final Object startupShutdownMonitor = new Object();

    private String id;

    private ServletContext servletContext;

    private String configLocation;

    private ApplicationContext parent;

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
        this.configLocation=configLocation;
    }

    @Override
    public void setEnvironment(ConfigurableEnvironment environment) {

    }

    @Override
    public ConfigurableEnvironment getEnvironment() {
        return null;
    }

    @Override
    public void setParent(ApplicationContext parent) {
        this.parent=parent;
    }

    @Override
    public void refresh() {

        synchronized (startupShutdownMonitor) {

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
}
