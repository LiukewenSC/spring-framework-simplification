package com.kewen.spring.context.event;

import com.kewen.spring.context.ApplicationContext;
import com.kewen.spring.context.ApplicationEvent;

/**
 * @descrpition  包装一个载荷事件发布器
 * @author kewen
 * @since 2023-02-07 17:24
 */
public class PayloadApplicationEvent<T> extends ApplicationEvent {

    private T payload;

    public PayloadApplicationEvent(ApplicationContext source, T payload) {
        super(source);
        this.payload = payload;
    }

    public T getPayload() {
        return payload;
    }
}
