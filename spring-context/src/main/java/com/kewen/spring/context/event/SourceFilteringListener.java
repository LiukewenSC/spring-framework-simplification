package com.kewen.spring.context.event;

import com.kewen.spring.context.ApplicationEvent;
import com.kewen.spring.context.ApplicationListener;
import com.kewen.spring.core.ResolvableType;
import com.kewen.spring.core.lang.Nullable;

/**
 * @descrpition 默认的监听器，在webmvc中会在配置刷新factory时new出来加入上下文中
 * @author kewen
 * @since 2023-03-24
 */
public class SourceFilteringListener implements GenericApplicationListener {
    /**
     * 原，一般是 ConfigurableWebApplicationContext 实例对象
     */
    private final Object source;
    /**
     * 监听器
     */
    private final ApplicationListener delegate;
    /**
     * 实际支持的事件类型
     */
    @Nullable
    private final ResolvableType declaredEventType;

    public SourceFilteringListener(Object source,ApplicationListener<? extends ApplicationEvent> applicationListener) {
        this.source = source;
        this.delegate = applicationListener;
        //原本要构造一个 GenericApplicationListenerAdapter ，这里就不绕了，不去构造了
        //获取到监听器中对应的泛型的实际类型(事件类型)并保存下来，供后续判断是否支持此事件类型
        this.declaredEventType=ResolvableType.as(applicationListener.getClass(),ApplicationEvent.class);
    }

    public boolean supportsEventType(ResolvableType eventType) {
        return this.declaredEventType.isAssignableFrom(eventType);
    }

    @Override
    public boolean supportsSourceType(Class<?> sourceType) {
        return (sourceType != null && sourceType.isInstance(this.source));
    }



    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        delegate.onApplicationEvent(event);
    }


}
