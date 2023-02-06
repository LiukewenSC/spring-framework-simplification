package com.kewen.spring.core.io;
/**
 * @descrpition 资源载入器
 * @author kewen
 * @since 2023-02-06 14:19
 */
public interface ResourceLoader {

    String CLASSPATH_URL_PREFIX = "classpath:";
    String CLASSPATH_ALL_URL_PREFIX = "classpath*:";

    Resource getResource(String location);
}
