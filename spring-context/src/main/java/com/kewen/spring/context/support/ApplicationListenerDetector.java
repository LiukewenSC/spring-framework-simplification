package com.kewen.spring.context.support;

import com.kewen.spring.beans.exception.BeansException;
import com.kewen.spring.beans.factory.ConfigurableListableBeanFactory;
import com.kewen.spring.beans.factory.config.BeanPostProcessor;
import com.kewen.spring.context.ApplicationContext;
import com.kewen.spring.context.ApplicationListener;
import com.kewen.spring.core.lang.Nullable;
import javafx.application.Application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @descrpition 上下文监听器注册器，将容器中的ApplicationListener添加到上下文中
 * 添加上下文应用监听器，此用用监听器在两处有加载，
 * 第一处是在{@link com.kewen.spring.context.support.AbstractApplicationContext#prepareBeanFactory(ConfigurableListableBeanFactory)}
 *      时加载，放至beanPostProcessors的第二个位置，用于处理系统的其他BeanPostProcessor实例化的时候使用的
 * 第二处是在{@link com.kewen.spring.context.support.AbstractApplicationContext#registerBeanPostProcessors(ConfigurableListableBeanFactory)}
 *      时加载，放至beanPostProcessors的最后，覆盖掉第一处加入的 ApplicationListenerDetector，因为此时BeanPostProcessor均已经加载完成，原来的监听器就清除了
 *
 * 上下文中加入时会首先移除再添加，因此，此类需要重写hashcode和equals
 *
 * @author kewen
 * @since 2023-02-07 10:18
 */
public class ApplicationListenerDetector implements BeanPostProcessor {

    private final AbstractApplicationContext applicationContext;

    private final Map<String,Boolean> singletonNames = new HashMap<>();

    public ApplicationListenerDetector(AbstractApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ApplicationListener){
            //原框架在BeanDefinition阶段根据是否为单例的bean加载beanName到singletonNames，
            // 现在框架省去了MergeBeanPostProcessor，就简易实现了，都统一视为单例的bean
            singletonNames.put(beanName,Boolean.TRUE);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ApplicationListener){
            //此处相当于会将所有的容器中的 ApplicationListener 加入到监听器中，从而加入到上下文中
            if (Boolean.TRUE.equals(singletonNames.get(beanName))){
                applicationContext.addApplicationListener(((ApplicationListener<?>) bean));
            }
        }
        return bean;
    }

    @Override
    public boolean equals(@Nullable Object other) {
        return (this == other || (other instanceof ApplicationListenerDetector &&
                this.applicationContext == ((ApplicationListenerDetector) other).applicationContext));
    }

    @Override
    public int hashCode() {
        return this.applicationContext.hashCode();
    }
}
