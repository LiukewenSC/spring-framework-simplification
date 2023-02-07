package com.kewen.spring.context.support;

import com.kewen.spring.beans.factory.config.BeanPostProcessor;
import com.kewen.spring.context.ApplicationContext;
import javafx.application.Application;

/**
 * @descrpition 上下文监听器
 * @author kewen
 * @since 2023-02-07 10:18
 */
public class ApplicationListenerDetector implements BeanPostProcessor {

    private final ApplicationContext applicationContext;

    public ApplicationListenerDetector(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
