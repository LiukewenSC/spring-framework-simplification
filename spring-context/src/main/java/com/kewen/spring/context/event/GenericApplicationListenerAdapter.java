package com.kewen.spring.context.event;

import com.kewen.spring.context.ApplicationEvent;
import com.kewen.spring.context.ApplicationListener;
import com.kewen.spring.core.ResolvableType;
import com.kewen.spring.core.lang.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @descrpition  代理的通用监听器实现
 * @author kewen
 * @since 2023-03-24
 */
public class GenericApplicationListenerAdapter implements GenericApplicationListener {

    private final ApplicationListener listener;
    @Nullable
    private final ResolvableType declaredEventType;

    public GenericApplicationListenerAdapter(ApplicationListener listener) {
        this.listener = listener;
        this.declaredEventType=ResolvableType.as(listener.getClass(),Object.class);
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        listener.onApplicationEvent(event);
    }

    @Override
    public boolean supportsEventType(ResolvableType eventType) {
        return declaredEventType.isAssignableFrom(eventType);
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        //原本还要判断 SmartApplicationListener的，这里就不判断了
        return GenericApplicationListener.super.supportsSourceType(sourceType);
    }
}
