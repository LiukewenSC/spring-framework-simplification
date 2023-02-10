package com.kewen.spring.beans.factory.xml;

import com.kewen.spring.beans.exception.BeansException;
import com.kewen.spring.beans.factory.config.BeanDefinition;
import com.kewen.spring.beans.factory.config.BeanDefinitionHolder;
import com.kewen.spring.beans.factory.config.GenericBeanDefinition;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.core.util.ClassUtils;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author kewen
 * @descrpition xml解析器
 * @since 2023-02-06 16:36
 */
public class BeanDefinitionParserDelegate {

    @Nullable
    public BeanDefinitionHolder parseBeanDefinitionElement(Map<String, Object> beanMap) {
        //
        String beanClass = (String) beanMap.get("class");
        String id = (String) beanMap.get("id");
        String beanName;
        if (id == null || id.trim().equals("")) {
            beanName = beanClass;
        } else {
            beanName = id;
        }

        BeanDefinition beanDefinition = createBeanDefinition(beanName, beanMap);
        String nameAttr = (String) beanMap.get("name");
        String[] aliases;
        if (nameAttr != null && nameAttr.trim().equals("")) {
            aliases = nameAttr.split(",");
        } else {
            aliases = null;
        }
        return new BeanDefinitionHolder(beanDefinition, beanName, aliases);

    }

    private BeanDefinition createBeanDefinition(@Nullable String beanName, Map<String, Object> beanMap) {
        BeanDefinition definition = new GenericBeanDefinition();
        String className = (String)beanMap.get("class");
        definition.setBeanClassName(className);
        String parentName = (String) beanMap.get("parent");
        definition.setParentName(parentName);
        Boolean primary = (Boolean) beanMap.get("primary");
        if (primary !=null){
            definition.setPrimary(primary);
        }
        String scope = (String) beanMap.get("scope");
        definition.setScope(scope);
        //还有一部分先暂时不管

        return definition;
    }


}
