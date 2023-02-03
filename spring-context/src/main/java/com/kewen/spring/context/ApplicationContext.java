package com.kewen.spring.context;

import com.kewen.spring.beans.factory.BeanFactory;
import com.kewen.spring.core.ConfigurableEnvironment;
import com.kewen.spring.core.lang.Nullable;

public interface ApplicationContext extends BeanFactory {

    String getApplicationName();

    /**
     * 可以为空
     * @return
     */
    @Nullable
    ApplicationContext getParent();

    void setParent(@Nullable ApplicationContext parent);

    void setConfigLocation(String configLocation);

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



}
