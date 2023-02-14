package com.kewen.spring.beans.factory.xml;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.lang.hash.Hash;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.SystemPropsUtil;
import com.kewen.spring.beans.BeanUtils;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.core.util.ClassUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @descrpition 默认的解析器 会加载META-INF下的spring.handlers文件中的解析器，自己实现了的话就在这里面加吧，可以在这里加载进来解析
 * 调试诡异事件见https://blog.csdn.net/qq_36908872/article/details/114464528
 * @author kewen
 * @since 2023-02-14 15:26
 */
public class DefaultNamespaceHandlerResolver implements NamespaceHandlerResolver {
    public static final String DEFAULT_HANDLER_MAPPINGS_LOCATION = "META-INF/spring.handlers";
    @Nullable
    private volatile Map<String, NamespaceHandler> handlerMappings =new ConcurrentHashMap<>();

    @Override
    public NamespaceHandler resolve(String namespaceUri) {
        NamespaceHandler namespaceHandler = getHandlerMappings().get(namespaceUri);
        return namespaceHandler;
    }

    /**
     * 获取handlerMappings
     *  为啥要懒加载，不直接在实例化的时候就把值读进去呢
     * @return
     */
    public Map<String, NamespaceHandler> getHandlerMappings() {
        if (this.handlerMappings.isEmpty()) {
            synchronized (this) {
                if (this.handlerMappings.isEmpty()) {
                    Properties properties = new Properties();
                    InputStream stream = ResourceUtil.getStream(DEFAULT_HANDLER_MAPPINGS_LOCATION);
                    try {
                        properties.load(stream);
                        Set<Map.Entry<Object, Object>> entries = properties.entrySet();
                        for (Map.Entry<Object, Object> entry : entries) {
                            String namespaceClass = (String) entry.getValue();
                            Class<Object> objectClass = ClassUtils.forName(namespaceClass);
                            NamespaceHandler namespaceHandler = (NamespaceHandler) BeanUtils.instantiateClass(objectClass);
                            namespaceHandler.init();
                            this.handlerMappings.put((String) entry.getKey(), namespaceHandler);
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return this.handlerMappings;
    }

    //@Override
    //public String toString() {
    //    return "NamespaceHandlerResolver using mappings " + getHandlerMappings();
    //}
}
