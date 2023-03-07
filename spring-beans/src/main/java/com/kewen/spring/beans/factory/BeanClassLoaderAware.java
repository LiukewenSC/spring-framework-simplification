package com.kewen.spring.beans.factory;
/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-07
 */
public interface BeanClassLoaderAware extends Aware{
    void setBeanClassLoader(ClassLoader classLoader);
}
