package com.kewen.spring.beans.factory.xml;

import com.kewen.spring.beans.factory.config.BeanDefinition;
import com.kewen.spring.beans.factory.config.BeanDefinitionHolder;
import com.kewen.spring.beans.factory.support.BeanDefinitionRegistry;
import com.kewen.spring.core.lang.Nullable;
import org.w3c.dom.Element;

/**
 * @descrpition bean定义解析器
 *
 * 自定义解析器，解析context标签
 * <context:component-scan base-package="com.kewen"/>
 * <context:load-time-weaver/>
 * <context:property-override/>
 * <context:mbean-server/>
 * <context:annotation-config/>
 * <context:property-placeholder/>
 * <context:spring-configured/>
 * <context:mbean-export/>
 *
 * @author kewen
 * @since 2023-02-14 10:51
 */
public interface BeanDefinitionParser {
    @Nullable
    BeanDefinition parse(Element element, BeanDefinitionRegistry registry);
}
