package com.kewen.spring.beans.factory.xml;

import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.kewen.spring.beans.exception.BeansException;
import com.kewen.spring.beans.factory.support.BeanDefinitionDocumentReader;
import com.kewen.spring.beans.factory.support.BeanDefinitionReader;
import com.kewen.spring.beans.factory.support.BeanDefinitionRegistry;
import com.kewen.spring.core.env.Environment;
import com.kewen.spring.core.io.Resource;
import com.kewen.spring.core.io.ResourceLoader;
import com.sun.istack.internal.Nullable;
import org.w3c.dom.Document;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-02-06 14:11
 */
public class XmlBeanDefinitionReader implements BeanDefinitionReader {
    private BeanDefinitionRegistry beanDefinitionRegistry;

    private Set<Resource> currentResources = new HashSet<>();
    @Nullable
    private ResourceLoader resourceLoader;

    @Nullable
    private ClassLoader beanClassLoader;

    private Environment environment;

    public XmlBeanDefinitionReader(BeanDefinitionRegistry beanDefinitionRegistry) {
        this.beanDefinitionRegistry = beanDefinitionRegistry;
    }

    @Override
    public int loadBeanDefinitions(String configLocation) {
        ResourceLoader resourceLoader = getResourceLoader();

        //configLocation  ->  classpath:applicationContext.xml
        Resource resource = resourceLoader.getResource(configLocation);

        int load = loadBeanDefinitions(resource);

        return load;
    }

    @Override
    public int loadBeanDefinitions(Resource resource) {
        if (!currentResources.add(resource)) {
            throw new BeansException(
                    "Detected cyclic loading of " + resource + " - check your import definitions!");
        }

        //注册解析内容到BeanDefinitionRegistry中  此处精简了非常多的内容，逻辑整体为 将解析的xml注册到BeanDefinitionRegistry中，
        InputStream inputStream = resource.getInputStream();
        Document document = XmlUtil.creatDocument(inputStream);

        //注册整个文档中的bean
        DefaultBeanDefinitionDocumentReader reader = new DefaultBeanDefinitionDocumentReader();
        int registerCount = reader.registerBeanDefinitions(document, beanDefinitionRegistry);

        return registerCount;
    }

    public BeanDefinitionRegistry getBeanDefinitionRegistry() {
        return beanDefinitionRegistry;
    }

    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    public ClassLoader getBeanClassLoader() {
        return beanClassLoader;
    }

    public void setBeanClassLoader(ClassLoader beanClassLoader) {
        this.beanClassLoader = beanClassLoader;
    }

    public Environment getEnvironment() {
        return environment;
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
