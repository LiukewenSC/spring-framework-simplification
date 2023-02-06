package com.kewen.spring.beans.factory.support;

import com.kewen.spring.core.env.Environment;
import com.kewen.spring.core.io.Resource;
import com.kewen.spring.core.io.ResourceLoader;

/**
 * @descrpition Bean定义加载器
 * @author kewen
 * @since 2023-02-06 14:11
 */
public interface BeanDefinitionReader {

    int loadBeanDefinitions(String configLocation);
    int loadBeanDefinitions(Resource resource);
    void setEnvironment(Environment environment);
    void setResourceLoader(ResourceLoader resourceLoader);

}
