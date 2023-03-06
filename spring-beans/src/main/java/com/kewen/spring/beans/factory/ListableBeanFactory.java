package com.kewen.spring.beans.factory;

import com.kewen.spring.beans.exception.BeansException;
import com.kewen.spring.core.lang.Nullable;

import java.util.List;
import java.util.Map;

/**
 * @descrpition 清单列表工厂
 * @author kewen
 * @since 2023-02-07
 */
public interface ListableBeanFactory extends BeanFactory {
    boolean containsBeanDefinition(String beanName);

    int getBeanDefinitionCount();

    String[] getBeanDefinitionNames();

    List<String> getBeanNamesForType(@Nullable Class<?> type, boolean includeNonSingletons, boolean allowEagerInit);
    <T> Map<String, T> getBeansOfType(@Nullable Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
            throws BeansException;
}
