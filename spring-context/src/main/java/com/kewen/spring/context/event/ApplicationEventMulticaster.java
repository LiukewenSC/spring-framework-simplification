package com.kewen.spring.context.event;

import com.kewen.spring.context.ApplicationEvent;
import com.kewen.spring.context.ApplicationListener;

/**
 * @descrpition 事件控制器
 * @author kewen
 * @since 2023-02-07 15:51
 */
public interface ApplicationEventMulticaster {
    void addApplicationListener(ApplicationListener<?> listener);
    void addApplicationListenerBean(String beanName);

    void multicastEvent(ApplicationEvent event);
}
