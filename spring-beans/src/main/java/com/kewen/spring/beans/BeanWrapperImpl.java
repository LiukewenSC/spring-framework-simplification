package com.kewen.spring.beans;

/**
 * @author kewen
 * @descrpition
 * @since 2023-02-10 17:53
 */
public class BeanWrapperImpl implements BeanWrapper{

    private Object wrappedObject;

    public BeanWrapperImpl(Object instance) {
        this.wrappedObject = instance;
    }

    @Override
    public Object getWrappedInstance() {
        return wrappedObject;
    }

    @Override
    public Class<?> getWrappedClass() {
        return wrappedObject.getClass();
    }
}
