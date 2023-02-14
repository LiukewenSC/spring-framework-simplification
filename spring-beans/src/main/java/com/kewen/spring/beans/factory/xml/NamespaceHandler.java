package com.kewen.spring.beans.factory.xml;

import com.kewen.spring.beans.factory.config.BeanDefinition;
import com.kewen.spring.beans.factory.config.BeanDefinitionHolder;
import com.kewen.spring.beans.factory.support.BeanDefinitionRegistry;
import com.kewen.spring.core.lang.Nullable;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * @descrpition 命名空间处理器
 * @author kewen
 * @since 2023-02-14 14:48
 */
public interface NamespaceHandler {
    void init();
    @Nullable
    BeanDefinition parse(Element element, BeanDefinitionRegistry registry);
    BeanDefinitionHolder decorate(Node source, BeanDefinitionHolder definition,BeanDefinitionRegistry registry);
}
