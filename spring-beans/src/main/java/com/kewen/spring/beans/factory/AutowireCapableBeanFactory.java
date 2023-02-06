package com.kewen.spring.beans.factory;
/**
 * @descrpition 自动注入工厂
 * @author kewen
 * @since 2023-02-06 17:41
 */
public interface AutowireCapableBeanFactory {

    int AUTOWIRE_NO = 0;


    int AUTOWIRE_BY_NAME = 1;


    int AUTOWIRE_BY_TYPE = 2;


    int AUTOWIRE_CONSTRUCTOR = 3;

    @Deprecated
    int AUTOWIRE_AUTODETECT = 4;

    String ORIGINAL_INSTANCE_SUFFIX = ".ORIGINAL";
}
