package com.kewen.spring.core.util;

import com.kewen.spring.core.lang.Nullable;

import java.lang.reflect.Method;

public class ClassUtils {

    public static <T> Class<T> forName(String className) throws ClassNotFoundException {
        ClassLoader classLoader = getDefaultClassLoader();
        return (Class<T>)classLoader.loadClass(className);
    }


    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;

        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable var3) {
        }

        if (cl == null) {
            cl = ClassUtils.class.getClassLoader();
            if (cl == null) {
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable var2) {
                }
            }
        }
        if (cl == null) {
            throw new RuntimeException("classloader load failed");
        }
        return cl;
    }

    public static Method getMethodIfAvailable(Class<?> aClass, String initMethodName,Class<?>... paramTypes) {
        try {
            Method method = aClass.getDeclaredMethod(initMethodName, paramTypes);
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            return method;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }
}
