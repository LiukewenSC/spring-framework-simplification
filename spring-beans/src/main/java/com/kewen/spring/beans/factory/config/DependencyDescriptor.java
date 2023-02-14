package com.kewen.spring.beans.factory.config;

import java.lang.reflect.Field;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-02-15
 */
public class DependencyDescriptor {

    private Class declaringClass;
    private String fieldName;
    private boolean required;
    private boolean eager;

    public DependencyDescriptor(Field field, boolean required) {
        this(field,required,true);
    }
    public DependencyDescriptor(Field field, boolean required, boolean eager) {

        this.declaringClass = field.getDeclaringClass();
        this.fieldName = field.getName();
        this.required = required;
        this.eager = eager;
    }
}
