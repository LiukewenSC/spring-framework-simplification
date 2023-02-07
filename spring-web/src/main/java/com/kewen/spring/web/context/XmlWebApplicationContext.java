package com.kewen.spring.web.context;

import com.kewen.spring.beans.exception.BeansException;
import com.kewen.spring.beans.factory.ConfigurableListableBeanFactory;
import com.kewen.spring.beans.factory.DefaultListableBeanFactory;
import com.kewen.spring.beans.factory.config.BeanPostProcessor;
import com.kewen.spring.beans.factory.support.AbstractBeanFactory;
import com.kewen.spring.beans.factory.xml.XmlBeanDefinitionReader;
import com.kewen.spring.context.ApplicationContext;
import com.kewen.spring.context.support.AbstractApplicationContext;
import com.kewen.spring.context.support.ApplicationContextAwareProcessor;
import com.kewen.spring.context.support.ApplicationListenerDetector;
import com.kewen.spring.core.ConfigurableEnvironment;
import com.kewen.spring.core.io.ClassPathResource;
import com.kewen.spring.core.io.Resource;
import com.kewen.spring.core.io.ResourceLoader;
import com.kewen.spring.core.lang.Nullable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class XmlWebApplicationContext extends AbstractApplicationContext implements ConfigurableWebApplicationContext {


    private ServletContext servletContext;	/** Servlet config that this context runs in, if any. */
    @Nullable
    private ServletConfig servletConfig;

    @Override
    public ServletContext getServletContext() {
        return servletContext;
    }

    @Override
    public void setServletContext(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    @Override
    public ServletConfig getServletConfig() {
        return servletConfig;
    }

    @Override
    public void setServletConfig(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }

    /**
     * 加载BeanDefinition，这个很重要
     * @param beanFactory
     */
    public void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws IOException {
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

    @Override
    protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {

        //暂时不知道干嘛用
        beanFactory.addBeanPostProcessor(new ServletContextAwareProcessor(this.servletContext, this.servletConfig));

        //添加servlet处理器，注册域scope和环境bean 暂时略掉

    }
    @Override
    protected void onRefresh() throws BeansException {

    }
}
