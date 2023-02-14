package com.kewen.spring.context.annotation;

import com.kewen.spring.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.kewen.spring.beans.factory.config.BeanDefinitionHolder;
import com.kewen.spring.beans.factory.config.RootBeanDefinition;
import com.kewen.spring.beans.factory.support.BeanDefinitionRegistry;
import com.kewen.spring.core.lang.Nullable;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @descrpition 注解处理工具，目前发现用于注入必要的配置类
 * @author kewen
 * @since 2023-02-15
 */
public class AnnotationConfigUtils {
    public static final String AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME =
            "org.springframework.context.annotation.internalAutowiredAnnotationProcessor";

    public static Set<BeanDefinitionHolder> registerAnnotationConfigProcessors(BeanDefinitionRegistry registry) {

        Set<BeanDefinitionHolder> definitionHolders = new LinkedHashSet<>(8);

        if (!registry.containsBeanDefinition(AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME)) {
            RootBeanDefinition autowiredDefinition = new RootBeanDefinition(AutowiredAnnotationBeanPostProcessor.class);
            registry.registerBeanDefinition(AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME,autowiredDefinition);
            BeanDefinitionHolder autowiredDefinitionHolder = new BeanDefinitionHolder(
                    autowiredDefinition, AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME, null
            );
            definitionHolders.add(autowiredDefinitionHolder);

        }

        return definitionHolders;
    }
}
