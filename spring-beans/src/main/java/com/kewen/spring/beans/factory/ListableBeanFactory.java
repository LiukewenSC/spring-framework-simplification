package com.kewen.spring.beans.factory;

import com.kewen.spring.core.lang.Nullable;

/**
 * @descrpition 清单列表工厂
 * @author kewen
 * @since 2023-02-07 14:41
 */
public interface ListableBeanFactory extends BeanFactory {
    boolean containsBeanDefinition(String beanName);

    int getBeanDefinitionCount();

    String[] getBeanDefinitionNames();

    String[] getBeanNamesForType(@Nullable Class<?> type, boolean includeNonSingletons, boolean allowEagerInit);
}
