package com.kewen.spring.context;

import java.util.EventListener;

/**
 * @descrpition 应用监听器
 * @author kewen
 * @since 2023-02-07 16:06
 */
public interface ApplicationListener<T extends ApplicationEvent> extends EventListener {

}