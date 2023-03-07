package com.kewen.spring.context;

import com.kewen.spring.beans.exception.BeansException;
import com.kewen.spring.beans.factory.Aware;

/**
 * @descrpition 上下文配置
 * @author kewen
 * @since 2023-03-07
 */
public interface ApplicationContextAware extends Aware {
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}
