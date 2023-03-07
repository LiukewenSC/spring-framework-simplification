package com.kewen.spring.context.support;

import com.kewen.spring.beans.exception.BeansException;
import com.kewen.spring.beans.factory.config.BeanPostProcessor;
import com.kewen.spring.context.ApplicationContext;
import com.kewen.spring.context.ApplicationContextAware;
import com.kewen.spring.context.ConfigurableApplicationContext;
import com.kewen.spring.context.EnvironmentAware;

/**
 * @descrpition 上下文处理器
 * @author kewen
 * @since 2023-02-07 9:55
 */
public class ApplicationContextAwareProcessor implements BeanPostProcessor {

    private final ConfigurableApplicationContext applicationContext;

    public ApplicationContextAwareProcessor(ConfigurableApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof EnvironmentAware) {
            ((EnvironmentAware) bean).setEnvironment(this.applicationContext.getEnvironment());
        }

        //中间还有几个暂时不管
        // ......

        //上下文加载
        if (bean instanceof ApplicationContextAware){
            ((ApplicationContextAware) bean).setApplicationContext(applicationContext);
        }

        return bean;

    }
}
