package com.kewen.spring.beans.factory.xml;

import com.kewen.spring.beans.factory.config.BeanDefinition;
import com.kewen.spring.beans.factory.config.BeanDefinitionHolder;
import com.kewen.spring.beans.factory.support.BeanDefinitionRegistry;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.Map;

/**
 * @descrpition  抽象的命名空间处理器，其beanDefinition解析器由子类实现
 * 通过类似于spi的方式解析classpath:META-INF/spring.handlers得到命名空间解析器实现
 *  为啥不是Abstract开头的，乱整吗？
 * @author kewen
 * @since 2023-02-14
 */
public abstract class NamespaceHandlerSupport implements NamespaceHandler {
    /**
     * 自定义解析器，解析context标签
     * <context:component-scan base-package="com.kewen"/>
     * <context:load-time-weaver/>
     * <context:property-override/>
     * <context:mbean-server/>
     * <context:annotation-config/>
     * <context:property-placeholder/>
     * <context:spring-configured/>
     * <context:mbean-export/>
     */
    private final Map<String, BeanDefinitionParser> parsers = new HashMap<>();
    protected final void registerBeanDefinitionParser(String elementName, BeanDefinitionParser parser) {
        this.parsers.put(elementName, parser);
    }
    @Override
    public BeanDefinition parse(Element element, BeanDefinitionRegistry registry) {
        BeanDefinition parse = parsers.get(element.getLocalName()).parse(element, registry);
        return parse;
    }

    @Override
    public BeanDefinitionHolder decorate(Node source, BeanDefinitionHolder definition, BeanDefinitionRegistry registry) {
        return null;
    }
}
