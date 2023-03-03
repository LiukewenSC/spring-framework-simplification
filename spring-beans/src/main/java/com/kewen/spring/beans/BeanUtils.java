package com.kewen.spring.beans;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import com.kewen.spring.beans.exception.BeanInstantiationException;
import com.kewen.spring.beans.factory.BeanFactory;
import com.kewen.spring.core.util.Assert;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class BeanUtils {
    public static <T> T instantiateClass(Class<T> aClass) {
        return instantiateClass(aClass, (Object[])null);
    }
    public static <T> T instantiateClass(Class<T> aClass,Object[] args) {

        try {
            if (args ==null){
                return aClass.newInstance();
            } else {
                Class<?>[] classes = new Class[args.length];
                for (int i = 0; i < args.length; i++) {
                    classes[i]=args[i].getClass();
                }
                Constructor<T> constructor = aClass.getConstructor(classes);
                return constructor.newInstance(args);
            }
        } catch (Exception e) {
            throw new BeanInstantiationException("instantiateClass exception",e);
        }
    }

    /**
     * 处理构造器实例化的情况，利用构造器实例化需要传入beanfactory以获取到实例参数
     * @param beanFactory
     * @param aClass
     * @param <T>
     * @return
     */
    public static <T> T instantiateClass(BeanFactory beanFactory,Class<T> aClass) {
        Constructor<?>[] constructors = aClass.getConstructors();
        Constructor<?> constructor = Arrays.stream(constructors).sorted(new Comparator<Constructor>() {
            @Override
            public int compare(Constructor o1, Constructor o2) {
                return o1.getParameterCount() - o2.getParameterCount();
            }
        }).findFirst().get();

        //没有参数就返回自己实例化
        if (constructor.getParameterCount()==0) {
            return instantiateClass(aClass);
        }

        //有参数则需要从bean工厂中获取并先实例化
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        Object[] parameters = new Object[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            parameters[i]=beanFactory.getBean(parameterTypes[i]);
        }
        try {
            return (T)constructor.newInstance(parameters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static <T> T instantiateClass(Class<?> clazz, Class<T> assignableTo) throws BeanInstantiationException {
        Assert.isAssignable(assignableTo, clazz);
        return (T) instantiateClass(clazz);
    }
}
