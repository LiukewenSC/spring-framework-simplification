package com.kewen.spring.context.support;

import com.kewen.spring.beans.factory.config.BeanPostProcessor;
import com.kewen.spring.context.ApplicationContext;

/**
 * @descrpition 上下文处理器
 * @author kewen
 * @since 2023-02-07 9:55
 */
public class ApplicationContextAwareProcessor implements BeanPostProcessor {

    private final ApplicationContext applicationContext;

    public ApplicationContextAwareProcessor(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }
}
