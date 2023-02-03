package com.kewen.spring.beans;

import com.kewen.spring.beans.exception.BeanInstantiationException;

public class BeanUtils {
    public static <T> T instantiateClass(Class<T> aClass) {

        try {
            return aClass.newInstance();
        } catch (Exception e) {
            throw new BeanInstantiationException("instantiateClass exception",e);
        }


    }
}
