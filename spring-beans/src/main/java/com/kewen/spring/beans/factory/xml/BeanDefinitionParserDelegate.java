package com.kewen.spring.beans.factory.xml;

import com.kewen.spring.beans.MutablePropertyValues;
import com.kewen.spring.beans.PropertyValue;
import com.kewen.spring.beans.factory.config.BeanDefinition;
import com.kewen.spring.beans.factory.config.BeanDefinitionHolder;
import com.kewen.spring.beans.factory.config.GenericBeanDefinition;
import com.kewen.spring.core.lang.Nullable;

import java.util.Collection;
import java.util.List;
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
        GenericBeanDefinition definition = new GenericBeanDefinition();
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

        //解析属性
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        Object property = beanMap.get("property");
        if(property ==null) {
            //为空不处理
        }else if (property instanceof Collection){
            List<Object> subs = (List<Object>)property;
            for (Object sub : subs) {
                PropertyValue propertyValue = pares2PropertyValue((Map) sub);
                propertyValues.addPropertyValueOrReplace(propertyValue);
            }
        } else {
            PropertyValue propertyValue = pares2PropertyValue((Map) property);
            propertyValues.addPropertyValueOrReplace(propertyValue);
        }
        definition.setPropertyValues(propertyValues);


        //还有一部分先暂时不管

        return definition;
    }
    private PropertyValue pares2PropertyValue(Map map){
        String name = (String)map.get("name");
        String ref = (String)map.get("ref");
        Object value = ref==null?map.get("value"):ref;
        return new PropertyValue(name,value);
    }


}
