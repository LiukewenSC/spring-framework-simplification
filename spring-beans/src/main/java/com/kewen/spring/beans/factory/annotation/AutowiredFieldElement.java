package com.kewen.spring.beans.factory.annotation;

import java.lang.reflect.Field;

/**
 * @descrpition 自动注入属性类，这里跟原来框架是不一样的
 * @author kewen
 * @since 2023-02-15
 */
public class AutowiredFieldElement extends AutowiredElement {
    private final Field field;


    public AutowiredFieldElement(Field field, boolean required,String qualifier) {
        super(field.getType(),required,qualifier);
        this.field=field;
        field.setAccessible(true);
    }

    @Override
    protected void setValue(Object bean, Object param) {
        try {
            field.set(bean,param);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
