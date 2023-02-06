package com.kewen.spring.beans.factory.config;

import com.kewen.spring.core.lang.Nullable;

/**
 * @descrpition bean定义的持有者
 * @author kewen
 * @since 2023-02-06 16:31
 */
public class BeanDefinitionHolder {
    private final BeanDefinition beanDefinition;

    private final String beanName;

    @Nullable
    private final String[] aliases;

    public BeanDefinitionHolder(BeanDefinition beanDefinition, String beanName, String[] aliases) {
        this.beanDefinition = beanDefinition;
        this.beanName = beanName;
        this.aliases = aliases;
    }

    public BeanDefinition getBeanDefinition() {
        return beanDefinition;
    }

    public String getBeanName() {
        return beanName;
    }

    public String[] getAliases() {
        return aliases;
    }
}
