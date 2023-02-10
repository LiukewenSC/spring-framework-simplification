package com.kewen.spring.beans.factory.config;

import com.kewen.spring.beans.BeanUtils;
import com.kewen.spring.beans.exception.BeansException;
import com.kewen.spring.beans.factory.AutowireCapableBeanFactory;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.core.util.ClassUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @descrpition 通用Bean定义
 * @author kewen
 * @since 2023-02-06 17:35
 */
public class GenericBeanDefinition extends AbstractBeanDefinition {

    private String parentName;

    @Override
    public String getParentName() {
        return parentName;
    }

    @Override
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
