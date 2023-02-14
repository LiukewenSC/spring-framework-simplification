package com.kewen.spring.beans.factory.xml;

import com.kewen.spring.core.lang.Nullable;

/**
 * @descrpition 命名空间处理器解析器
 * @author kewen
 * @since 2023-02-14 15:25
 */
public interface NamespaceHandlerResolver {
    @Nullable
    NamespaceHandler resolve(String namespaceUri);
}
