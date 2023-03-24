package com.kewen.spring.context.event;

import com.kewen.spring.context.ApplicationEvent;
import com.kewen.spring.context.ApplicationListener;
import com.kewen.spring.core.ResolvableType;
import com.kewen.spring.core.lang.Nullable;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-24
 */
public interface GenericApplicationListener extends ApplicationListener<ApplicationEvent> {
    boolean supportsEventType(ResolvableType eventType);

    default boolean supportsSourceType(@Nullable Class<?> sourceType) {
        return true;
    }
}
