package com.kewen.spring.context;

import com.kewen.spring.beans.factory.config.BeanFactoryPostProcessor;
import com.kewen.spring.core.ConfigurableEnvironment;
import com.kewen.spring.core.lang.Nullable;

/**
 * @descrpition 可配置的上下文
 * @author kewen
 * @since 2023-02-07 11:16
 */
public interface ConfigurableApplicationContext extends ApplicationContext {

    void setId(String id);


    void setParent(@Nullable ApplicationContext parent);
    /**
     * Set the {@code Environment} for this application context.
     * @param environment the new environment
     * @since 3.1
     */
    void setEnvironment(ConfigurableEnvironment environment);

    /**
     * Return the {@code Environment} for this application context in configurable
     * form, allowing for further customization.
     * @since 3.1
     */
    ConfigurableEnvironment getEnvironment();


    void addBeanFactoryPostProcessor(BeanFactoryPostProcessor postProcessor);

    void refresh() ;
}
