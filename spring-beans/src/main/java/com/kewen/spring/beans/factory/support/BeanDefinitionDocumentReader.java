package com.kewen.spring.beans.factory.support;

import cn.hutool.json.JSONArray;
import com.kewen.spring.core.io.Resource;
import org.w3c.dom.Document;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-02-06 16:23
 */
public interface BeanDefinitionDocumentReader {

    /**
     * 注册文档中的bean
     * @param document
     * @param registry
     * @return
     */
    int  registerBeanDefinitions(Document document, BeanDefinitionRegistry registry);

}
