package com.kewen.spring.beans.factory.xml;

import cn.hutool.json.JSONArray;
import com.kewen.spring.beans.factory.config.BeanDefinitionHolder;
import com.kewen.spring.beans.factory.support.BeanDefinitionDocumentReader;
import com.kewen.spring.beans.factory.support.BeanDefinitionRegistry;
import com.kewen.spring.core.lang.Nullable;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author kewen
 * @descrpition
 * @since 2023-02-06 16:26
 */
public class DefaultBeanDefinitionDocumentReader implements BeanDefinitionDocumentReader {

    @Nullable
    BeanDefinitionParserDelegate delegate;

    @Override
    public int registerBeanDefinitions(JSONArray objects, BeanDefinitionRegistry registry) {

        int beforeCount = registry.getBeanDefinitionCount();

        BeanDefinitionParserDelegate parent = this.delegate;

        this.delegate = createDelegate(parent);

        List<Map> maps = objects.toList(Map.class);
        for (Map<String, Object> beanMap : maps) {
            BeanDefinitionHolder beanDefinitionHolder = delegate.parseBeanDefinitionElement(beanMap);
            String beanName = beanDefinitionHolder.getBeanName();
            registry.registerBeanDefinition(beanName, beanDefinitionHolder.getBeanDefinition());
            registry.registerAlias(beanName, beanDefinitionHolder.getAliases());
        }

        int nowCount = registry.getBeanDefinitionCount();

        return nowCount - beforeCount;
    }

    private BeanDefinitionParserDelegate createDelegate(BeanDefinitionParserDelegate parent) {
        //原框架为复制 parent 的属性逻辑
        BeanDefinitionParserDelegate delegate = new BeanDefinitionParserDelegate();
        return delegate;
    }
}
