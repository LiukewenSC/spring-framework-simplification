package com.kewen.spring.context.event;

import com.kewen.spring.context.ApplicationContext;
import com.kewen.spring.context.support.AbstractApplicationContext;

/**
 * @descrpition 刷新上下文事件
 * @author kewen
 * @since 2023-02-07 17:14
 */
public class ContextRefreshedEvent extends ApplicationContextEvent {
    public ContextRefreshedEvent(ApplicationContext applicationContext) {
        super(applicationContext);
    }
}
