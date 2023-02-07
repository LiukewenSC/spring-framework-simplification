package com.kewen.spring.context.event;

import com.kewen.spring.context.ApplicationEvent;

/**
 * @descrpition 应用事件发布器
 * @author kewen
 * @since 2023-02-07 17:19
 */
public interface ApplicationEventPublisher {
    default void publishEvent(ApplicationEvent event) {
        publishEvent((Object) event);
    }

    void publishEvent(Object event);
}
