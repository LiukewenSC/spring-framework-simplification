package com.kewen.spring.context.event;

import com.kewen.spring.context.ApplicationEvent;
import com.kewen.spring.context.ApplicationListener;
import com.kewen.spring.core.ResolvableType;
import com.kewen.spring.core.lang.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
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
        multicastEvent(event,ResolvableType.forInstance(event));
    }

    @Override
    public void multicastEvent(ApplicationEvent event, ResolvableType resolvableType) {
        for (ApplicationListener applicationListener : getApplicationListeners(event,resolvableType)) {
            applicationListener.onApplicationEvent(event);
        }
    }

    /**
     * 获取事件对应的监听器
     * @param event
     * @param resolvableType
     * @return
     */
    protected Collection<ApplicationListener<?>> getApplicationListeners(ApplicationEvent event,ResolvableType resolvableType){
        Class<?> sourceType = event.getSource().getClass();
        List<ApplicationListener<?>> listeners = new ArrayList<>();

        //此处还有解析filteredListenerBeans等，暂时不管

        //同时省略了部分监听器的获取方法等，只留下核心流程
        for (ApplicationListener<?> listener : applicationListeners) {
            if (supportsEvent(listener,resolvableType,sourceType)) {
                listeners.add(listener);
            }
        }
        return listeners;
    }

    /**
     * 是否支持对应的事件
     * @param listener 监听器
     * @param eventType 事件类型，如 ApplicationRefreshEvent
     * @param sourceType 原类型，如 XmlApplicationContext
     * @return
     */
    protected boolean supportsEvent(
            ApplicationListener<?> listener, ResolvableType eventType, @Nullable Class<?> sourceType) {

        GenericApplicationListener adapter = (listener instanceof GenericApplicationListener ?
                (GenericApplicationListener) listener : new GenericApplicationListenerAdapter(listener));

        return adapter.supportsEventType(eventType) && adapter.supportsSourceType(sourceType);
    }
}
