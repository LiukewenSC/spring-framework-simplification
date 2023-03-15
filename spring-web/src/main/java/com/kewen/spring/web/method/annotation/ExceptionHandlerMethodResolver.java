package com.kewen.spring.web.method.annotation;

import cn.hutool.core.annotation.AnnotationUtil;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.web.bind.annotation.ExceptionHandler;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-15
 */
public class ExceptionHandlerMethodResolver {

    /**
     * 异常方法对应的缓存，实现通过异常类型找到对应的方法
     */
    private final Map<Class<? extends Throwable>, Method> mappedMethods = new HashMap<>(16);
    /**
     * 保存异常对应已经解析了的缓存，后续就不需要再解析了
     */
    private final Map<Class<? extends Throwable>, Method> exceptionLookupCache = new ConcurrentHashMap<>(16);


    /**
     * @param handlerType  @ControllerAdvice对应的类
     */
    public ExceptionHandlerMethodResolver(Class<?> handlerType) {
        for (Method method : handlerType.getMethods()) {
            if (AnnotationUtil.hasAnnotation(method, ExceptionHandler.class)) {
                mappedMethods.put(AnnotationUtil.getAnnotationValue(method,ExceptionHandler.class),method);
            }
        }
    }

    @Nullable
    public Method resolveMethod(Exception exception) {
        Method method = resolveMethodByException(exception);
        if (method==null){
            method =resolveMethodByException(exception.getCause());
        }
        return method;
    }
    @Nullable
    public Method resolveMethodByException(Throwable exception) {
        Class<? extends Throwable> exceptionType = exception.getClass();
        Method method = this.exceptionLookupCache.get(exceptionType);
        if (method == null) {
            method = getMappedMethod(exception);
            if (method !=null){
                this.exceptionLookupCache.put(exceptionType, method);
            }
        }
        return method;
    }

    /**
     * 获取异常类对应的方法
     * @param t
     * @return
     */
    @Nullable
    private Method getMappedMethod(Throwable t) {
        if (mappedMethods.size()==0){
            return null;
        }
        //获取异常对应的方法，优先找层级最下面的异常，如 RuntimeException -> Exception ->Throwable 则找到RuntimeException
        Map<Class<? extends Throwable>, Method> methodMap = new HashMap<>();
        List<Class<? extends Throwable>> keyList = new ArrayList<>(mappedMethods.keySet());
        keyList.sort((o1, o2) -> {
            int depth1 = getDepth(o1, t.getClass(), 0);
            int depth2 = getDepth(o2, t.getClass(), 0);
            return (depth1 - depth2);
        });
        return mappedMethods.get(keyList.get(0));
    }
    private int getDepth(Class<?> declaredException, Class<?> exceptionToMatch, int depth) {
        if (exceptionToMatch.equals(declaredException)) {
            // Found it!
            return depth;
        }
        // If we've gone as far as we can go and haven't found it...
        if (exceptionToMatch == Throwable.class) {
            return Integer.MAX_VALUE;
        }
        return getDepth(declaredException, exceptionToMatch.getSuperclass(), depth + 1);
    }

    public boolean hasExceptionMappings() {
        return !this.mappedMethods.isEmpty();
    }
}
