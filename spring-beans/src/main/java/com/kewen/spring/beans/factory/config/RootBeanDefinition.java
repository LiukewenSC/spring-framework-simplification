package com.kewen.spring.beans.factory.config;

/**
 * @author kewen
 * @descrpition
 * @since 2023-02-10 11:17
 */
public class RootBeanDefinition extends AbstractBeanDefinition {

    private String parentName;

    public RootBeanDefinition(BeanDefinition beanDefinition) {
        super(beanDefinition);
    }

    @Override
    public String getParentName() {
        return parentName;
    }

    @Override
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
