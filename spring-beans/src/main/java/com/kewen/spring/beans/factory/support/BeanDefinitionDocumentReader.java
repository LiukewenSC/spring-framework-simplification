package com.kewen.spring.beans.factory.support;

import cn.hutool.json.JSONArray;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-02-06 16:23
 */
public interface BeanDefinitionDocumentReader {

    void  registerBeanDefinitions(JSONArray objects,BeanDefinitionRegistry registry);

}
