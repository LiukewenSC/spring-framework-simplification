package com.kewen.spring.context;

import com.kewen.spring.beans.factory.BeanFactory;
import com.kewen.spring.beans.factory.ConfigurableListableBeanFactory;
import com.kewen.spring.beans.factory.config.BeanFactoryPostProcessor;
import com.kewen.spring.context.event.ApplicationEventPublisher;
import com.kewen.spring.core.ConfigurableEnvironment;
import com.kewen.spring.core.lang.Nullable;

import java.util.List;

/**
 * @descrpition 上下文，整合了spring中的 ApplicationContext 和 ConfigurableApplicationContext
 * @author kewen
 * @since 2023-02-05 9:55
 */
public interface ApplicationContext extends BeanFactory, ApplicationEventPublisher {

    String getId();

    String getApplicationName();

    /**
     * 可以为空
     * @return
     */
    @Nullable
    ApplicationContext getParent();


    void setConfigLocation(String configLocation);





}
