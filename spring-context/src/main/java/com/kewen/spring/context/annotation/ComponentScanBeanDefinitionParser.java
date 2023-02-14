package com.kewen.spring.context.annotation;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.kewen.spring.beans.MutablePropertyValues;
import com.kewen.spring.beans.factory.annotation.Autowired;
import com.kewen.spring.beans.factory.config.BeanDefinition;
import com.kewen.spring.beans.factory.config.GenericBeanDefinition;
import com.kewen.spring.beans.factory.support.BeanDefinitionRegistry;
import com.kewen.spring.beans.factory.xml.BeanDefinitionParser;
import com.kewen.spring.core.util.StringUtils;
import org.w3c.dom.Element;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @descrpition ComponentScan标签解析器
 *      在 ContextNamespaceHandler中定义，解析component-scan标签对应的包使用的
 * @author kewen
 * @since 2023-02-14
 */
public class ComponentScanBeanDefinitionParser implements BeanDefinitionParser {

    @Override
    public BeanDefinition parse(Element element, BeanDefinitionRegistry registry) {
        String basePackageStr = element.getAttribute("base-package");
        String[] basePackages = basePackageStr.split(",");
        Map<String,BeanDefinition> beanDefinitionMap = new HashMap<>();

        //注册扫描包
        for (String basePackage : basePackages) {
            Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(basePackage,Component.class);
            for (Class<?> clazz : classes) {
                String beanName = clazz.getAnnotation(Component.class).value();
                if (StringUtils.isEmpty(beanName)){
                    beanName = StrUtil.lowerFirst(clazz.getSimpleName());
                }
                BeanDefinition definition = parse(clazz);
                beanDefinitionMap.put(beanName,definition);
            }
        }
        for (Map.Entry<String, BeanDefinition> definitionEntry : beanDefinitionMap.entrySet()) {
            registry.registerBeanDefinition(definitionEntry.getKey(),definitionEntry.getValue());
        }

        //注册需要的注解解析器
        AnnotationConfigUtils.registerAnnotationConfigProcessors(registry);

        //不保存值，返回始终为null
        return null;
    }
    private BeanDefinition parse(Class<?> clazz){
        GenericBeanDefinition definition = new GenericBeanDefinition();

        definition.setBeanClass(clazz);

        //注解的不再这里添加 propertyValues ， 在 AutowiredAnnotationBeanPostProcessor 中会解析 并注入到bean中，与xml配置还不太一样

        return definition;
    }


    private String getPackageSearchPath(String basePackage){
        return basePackage.replaceAll("\\.", "/");
    }
    private Class<?> getClassByPath(String path){
        String className = getClassName(path);
        return ClassUtil.loadClass(className);
    }
    private String getClassName(String path){
        String substring = path.substring(path.indexOf("classes" )+ 8);
        substring = substring.replaceAll("\\.class","");
        substring = substring.replace("\\",".");
        return substring;
    }
}
