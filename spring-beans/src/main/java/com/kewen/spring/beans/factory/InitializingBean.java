package com.kewen.spring.beans.factory;
/**
 * @descrpition 初始化完成时的后操作
 * @author kewen
 * @since 2023-03-07
 */
public interface InitializingBean {
    void afterPropertiesSet() throws Exception;
}
