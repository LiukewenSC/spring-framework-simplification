package com.kewen.spring.beans.factory;

import com.kewen.spring.beans.exception.BeansException;

/**
 * @descrpition 对象工厂，用以延缓生成对象
 * @author kewen
 * @since 2023-02-07 16:25
 */
public interface ObjectFactory<T> {
    T getObject() throws BeansException;
}
