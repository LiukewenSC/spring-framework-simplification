package com.kewen.spring.context.annotation;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.ClassScanner;
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

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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

            //扫描Component注解，兼容处理，原来很复杂，算了
            Map<String,Class<?>> scanClasses = scanComponents(basePackage);

            for (Map.Entry<String, Class<?>> entry : scanClasses.entrySet()) {
                String beanName = entry.getKey();
                Class<?> clazz = entry.getValue();
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

    /**
     * 扫描Component注解，此处简单处理，原逻辑很复杂了， 扫描Component注解，若有其它注解以Component为注解如@Service，则继续扫描
     * @param basePackage
     * @return  beanName,Class类
     */
    private Map<String,Class<?>> scanComponents(String basePackage){
        try {
            //Annotation[] annotations = AnnotationUtil.getAnnotations(Service.class, true);
            return scanComponentsLoop(basePackage, Component.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private Map<String,Class<?>> scanComponentsLoop(String basePackage,Class<? extends Annotation> anno) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Set<Class<?>> classes = ClassScanner.scanAllPackageByAnnotation(basePackage, anno);

        HashMap<String,Class<?>> scanMap = new HashMap<>();

        //迭代扫描子注解，若子注解有为注解类型的，直接移除并记录下来下次继续扫描
        //装子注解的
        HashSet<Class<? extends Annotation>> subAnnos = new HashSet<>();
        for (Class<?> scanedClass : classes) {
            if (scanedClass.isAnnotation()){
                //添加到子注解集合中并从原来集合移除
                subAnnos.add((Class<? extends Annotation>)scanedClass);
            } else {
                //解析value值并添加到集合中
                Annotation annotation = scanedClass.getAnnotation(anno);
                Method method = annotation.annotationType().getMethod("value");
                Object invoke = method.invoke(annotation);
                String beanName = ((String) invoke);
                if (StringUtils.isEmpty(beanName)){
                    beanName = StrUtil.lowerFirst(scanedClass.getSimpleName());
                }
                scanMap.put(beanName,scanedClass);
            }
        }

        if (!subAnnos.isEmpty()){
            for (Class<? extends Annotation> subAnno : subAnnos) {
                //继续递归处理
                Map<String,Class<?>> subClass = scanComponentsLoop(basePackage,subAnno);
                scanMap.putAll(subClass);
            }
        }
        return scanMap;
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
