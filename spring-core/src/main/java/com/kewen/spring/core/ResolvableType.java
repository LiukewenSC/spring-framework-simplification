package com.kewen.spring.core;

import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.core.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-24
 */
public class ResolvableType {

    @Nullable
    private Class<?> resolved;

    private ResolvableType(@Nullable Class<?> clazz) {
        this.resolved = (clazz != null ? clazz : Object.class);
    }


    public static ResolvableType forInstance(Object instance) {
        Assert.notNull(instance, "Instance must not be null");
        //if (instance instanceof ResolvableTypeProvider) {
        //    ResolvableType type = ((ResolvableTypeProvider) instance).getResolvableType();
        //    if (type != null) {
        //        return type;
        //    }
        //}
        return ResolvableType.forClass(instance.getClass());
    }

    public static ResolvableType forClass(@Nullable Class<?> clazz) {
        return new ResolvableType(clazz);
    }

    /**
     * 利用jdk方法找到类上对应的泛型，
     *
     * @param source 需要被找到泛型的类 如{@link com.kewen.spring.web.context.ContextLoaderListener$ContextRefreshListener}
     *               实际上ContextRefreshListener implements ApplicationListener<ContextRefreshedEvent>
     * @param target 泛型中的类指定的泛型类型 如:ApplicationEvent
     * @return 返回指定泛型类型的子类对应的解析类型 如:ContextRefreshedEvent
     */
    public static ResolvableType as(Class<?> source, Class<?> target) {
        // 原来的太复杂了，不要了，看不懂
        // 这里简单处理，直接拿到source 下的target即可
        Type[] genericInterfaces = source.getGenericInterfaces();
        for (Type genericInterface : genericInterfaces) {
            for (Type typeArgument : ((ParameterizedType) genericInterface).getActualTypeArguments()) {
                if (typeArgument instanceof Class) {
                    Class classType = (Class) typeArgument;
                    if (target.isAssignableFrom((classType))) {
                        return ResolvableType.forClass(classType);
                    }
                }
            }
        }
        System.out.println("走这儿就出错了");
        throw new RuntimeException("走这儿就出错了");
    }


    public boolean isAssignableFrom(ResolvableType other) {
        //原来框架太复杂了，这里简化就仅仅匹配两个类型是否匹配
        return this.resolved.isAssignableFrom(other.resolved);
    }
}
