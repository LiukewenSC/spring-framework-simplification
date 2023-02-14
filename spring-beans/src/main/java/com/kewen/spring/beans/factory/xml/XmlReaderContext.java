package com.kewen.spring.beans.factory.xml;

import com.kewen.spring.beans.factory.support.BeanDefinitionRegistry;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-02-14 15:52
 */
public class XmlReaderContext {
    private final BeanDefinitionRegistry beanDefinitionRegistry;
    private final NamespaceHandlerResolver namespaceHandlerResolver;

    public XmlReaderContext(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
        this.namespaceHandlerResolver  = new DefaultNamespaceHandlerResolver();
    }

    public BeanDefinitionRegistry getBeanDefinitionRegistry() {
        return beanDefinitionRegistry;
    }

    public NamespaceHandlerResolver getNamespaceHandlerResolver() {
        return namespaceHandlerResolver;
    }
}
