package com.kewen.spring.beans.factory.annotation;

import cn.hutool.core.collection.CollectionUtil;
import com.kewen.spring.beans.PropertyValues;
import com.kewen.spring.beans.exception.BeansException;
import com.kewen.spring.beans.factory.ConfigurableListableBeanFactory;
import com.kewen.spring.beans.factory.SmartInstantiationAwareBeanPostProcessor;
import com.kewen.spring.beans.factory.config.RootBeanDefinition;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @descrpition 此处主要解析 @Autowired 注解对应的属性，需要加工成PropertyValues
 * @author kewen
 * @since 2023-02-15
 */
public class AutowiredAnnotationBeanPostProcessor implements SmartInstantiationAwareBeanPostProcessor {
    private ConfigurableListableBeanFactory beanFactory;
    private final Map<String, InjectionMetadata> injectionMetadataCache = new ConcurrentHashMap<>(256);
    private final Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet<>(4);

    public AutowiredAnnotationBeanPostProcessor(ConfigurableListableBeanFactory beanFactory) {
        autowiredAnnotationTypes.add(Autowired.class);
        autowiredAnnotationTypes.add(Value.class);
        this.beanFactory=beanFactory;
    }

    /**
     *
     * @param pvs
     * @param bean
     * @param beanName
     * @return null 返回为空，不会返回PropertyValues，此处已经注入完成了
     * @throws BeansException
     */
    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
        InjectionMetadata autowiringMetadata = findAutowiringMetadata(beanName, bean.getClass());
        try {
            List<AutowiredElement> elements = autowiringMetadata.getElements();
            if (CollectionUtil.isNotEmpty(elements)){
                for (AutowiredElement element : elements) {

                    Object autowiredBean = element.fillup(beanFactory,bean);

                }
            }

        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        return pvs;
    }

    /**
     * 查找自动注入的metadata
     * @param beanName
     * @param clazz
     * @return
     */
    private InjectionMetadata findAutowiringMetadata(String beanName, Class<?> clazz) {
        InjectionMetadata injectionMetadata = injectionMetadataCache.get(beanName);
        if (injectionMetadata ==null ){
            injectionMetadata = buildAutowiringMetadata(clazz);
            injectionMetadataCache.put(beanName,injectionMetadata);
        }
        return injectionMetadata;
    }
    private InjectionMetadata buildAutowiringMetadata(final Class<?> clazz) {

        List<AutowiredElement> elements = new ArrayList<>();

        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            Autowired autowired = field.getAnnotation(Autowired.class);
            if (autowired !=null){
                String autowiredBeanName = Optional.ofNullable(field.getAnnotation(Qualifier.class)).map(Qualifier::value).orElse(null);
                elements.add(new AutowiredFieldElement(field,autowired.required(),autowiredBeanName));
            }
        }
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            Autowired autowired = method.getAnnotation(Autowired.class);
            if (autowired !=null){
                String autowiredBeanName = Optional.ofNullable(method.getAnnotation(Qualifier.class)).map(Qualifier::value).orElse(null);
                elements.add(new AutowiredMethodElement(method,autowired.required(),autowiredBeanName));
            }
        }
        InjectionMetadata metadata = new InjectionMetadata();
        metadata.setElements(elements);
        return metadata;
    }
}
