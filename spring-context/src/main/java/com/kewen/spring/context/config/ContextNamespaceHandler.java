package com.kewen.spring.context.config;

import com.kewen.spring.beans.factory.xml.NamespaceHandlerSupport;
import com.kewen.spring.context.annotation.ComponentScanBeanDefinitionParser;

/**
 * @descrpition  上下文解析器
 * @author kewen
 * @since 2023-02-14 14:54
 */
public class ContextNamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("component-scan", new ComponentScanBeanDefinitionParser());
    }
}
