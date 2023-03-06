package com.kewen.spring.beans.factory;

import com.kewen.spring.beans.exception.BeansException;

import java.util.Map;

/**
 * @author kewen
 * @descrpition
 * @since 2023-03-06
 */
public class BeanFactoryUtils {

    public static <T> Map<String, T> beansOfTypeIncludingAncestors (
            ListableBeanFactory lbf, Class<T> type, boolean includeNonSingletons, boolean allowEagerInit)
            throws BeansException {

        Map<String, T>  map= lbf.getBeansOfType(type,includeNonSingletons,allowEagerInit);

        // TODO: 2023/3/6 此处本来有循环递归找父工厂的数据，暂时简化
        return map;
    }
}
