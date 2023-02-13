package com.kewen.spring.beans.factory.xml;

import com.kewen.spring.beans.MutablePropertyValues;
import com.kewen.spring.beans.PropertyValue;
import com.kewen.spring.beans.factory.config.BeanDefinition;
import com.kewen.spring.beans.factory.config.BeanDefinitionHolder;
import com.kewen.spring.beans.factory.config.GenericBeanDefinition;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.core.util.StringUtils;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author kewen
 * @descrpition xml解析器
 * @since 2023-02-06 16:36
 */
public class BeanDefinitionParserDelegate {

    /**
     * 解析得到元素中的内容，得到 BeanDefinitionHolder
     * @param beanElement
     * @return
     */
    @Nullable
    public BeanDefinitionHolder parseBeanDefinitionElement(Element beanElement) {
        //

        String id = beanElement.getAttribute("id");
        String beanClass = beanElement.getAttribute("class");
        String name = beanElement.getAttribute("name");
        String value = beanElement.getAttribute("value");
        String ref = beanElement.getAttribute("ref");
        String parent = beanElement.getAttribute("parent");
        String primary = beanElement.getAttribute("primary");
        String scope = beanElement.getAttribute("scope");

        String beanName;
        if (id == null || id.trim().equals("")) {
            beanName = beanClass;
        } else {
            beanName = id;
        }

        GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
        beanDefinition.setBeanClassName(beanClass);
        beanDefinition.setParentName(parent);
        if (primary !=null && !"".equals(primary) ){
            boolean b = Boolean.parseBoolean(primary);
            beanDefinition.setPrimary(b);
        }
        beanDefinition.setScope(scope);

        //解析属性
        MutablePropertyValues propertyValues = new MutablePropertyValues();
        List<Element> children = XmlUtil.getChildren(beanElement);
        for (Element child : children) {
            String propertyName = child.getAttribute("name");
            String propertyRef = child.getAttribute("ref");
            Object propertyValue = StringUtils.isEmpty(propertyRef) ?child.getAttribute("value"):propertyRef;
            //此处还有可能有子元素列表，要注意
            propertyValues.add(propertyName,propertyValue);
        }
        beanDefinition.setPropertyValues(propertyValues);

        String nameAttr = name;
        String[] aliases;
        if (nameAttr != null && nameAttr.trim().equals("")) {
            aliases = nameAttr.split(",");
        } else {
            aliases = null;
        }
        return new BeanDefinitionHolder(beanDefinition, beanName, aliases);

    }

}
