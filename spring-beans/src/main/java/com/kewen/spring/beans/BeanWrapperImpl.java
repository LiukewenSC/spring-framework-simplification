package com.kewen.spring.beans;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

/**
 * @author kewen
 * @descrpition
 * @since 2023-02-10 17:53
 */
public class BeanWrapperImpl implements BeanWrapper{

    private final Object wrappedObject;

    private final BeanInfo beanInfo;

    public BeanWrapperImpl(Object instance) {
        this.wrappedObject = instance;
        //简化处理，此处简化了spring自带的定义逻辑，
        // 去掉了 CachedIntrospectionResults 等封装及调用对象
        // 直接使用 jdk的代替，否则太复杂了
        this.beanInfo = createBeanInfo(instance.getClass());
    }
    static BeanInfo createBeanInfo(Class<?> tclass){
        try {
            return Introspector.getBeanInfo(tclass);
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getWrappedInstance() {
        return wrappedObject;
    }

    @Override
    public Class<?> getWrappedClass() {
        return wrappedObject.getClass();
    }

    @Override
    public PropertyDescriptor[] getPropertyDescriptors() {
        return beanInfo.getPropertyDescriptors();
    }

    @Override
    public PropertyDescriptor getPropertyDescriptor(String propertyName) {
        PropertyDescriptor[] descriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : descriptors) {
            if (descriptor.getName().equals(propertyName)) {
                return descriptor;
            }
        }
        return null;
    }
}
