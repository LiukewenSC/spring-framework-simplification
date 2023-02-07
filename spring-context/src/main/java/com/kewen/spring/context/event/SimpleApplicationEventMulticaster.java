package com.kewen.spring.context.event;

import com.kewen.spring.context.ApplicationEvent;
import com.kewen.spring.context.ApplicationListener;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @descrpition 事件处理器实例
 * @author kewen
 * @since 2023-02-07 15:51
 */
public class SimpleApplicationEventMulticaster implements ApplicationEventMulticaster {

    private final Set<ApplicationListener<?>> applicationListeners = new LinkedHashSet<>();
    private final Set<String> applicationListenerBeans = new LinkedHashSet<>();
    private final Set<ApplicationEvent> applicationEvents = new LinkedHashSet<>();
    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        applicationListeners.add(listener);
    }

    @Override
    public void addApplicationListenerBean(String beanName) {
        applicationListenerBeans.add(beanName);
    }

    @Override
    public void multicastEvent(ApplicationEvent event) {
        applicationEvents.add(event);
    }
}
