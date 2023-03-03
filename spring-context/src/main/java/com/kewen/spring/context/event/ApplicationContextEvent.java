package com.kewen.spring.context.event;

import com.kewen.spring.context.ApplicationContext;
import com.kewen.spring.context.ApplicationEvent;

/**
 * @descrpition 应用上下文事件
 * @author kewen
 * @since 2023-02-07 17:09
 */
public abstract class ApplicationContextEvent extends ApplicationEvent {


    public ApplicationContextEvent(ApplicationContext applicationContext) {
        super(applicationContext);
    }
    public ApplicationContext getApplicationContext(){
        return ((ApplicationContext) getSource());
    }
}
