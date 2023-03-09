package com.kewen.spring.web.util;

import com.kewen.spring.core.lang.Nullable;

import javax.servlet.ServletRequest;
import javax.servlet.ServletRequestWrapper;

/**
 * @descrpition web工具
 * @author kewen
 * @since 2023-03-09
 */
public class WebUtils {
    /**
     * 递归获取本地请求
     * @return
     */
    public static <T> T getNativeRequest(ServletRequest request, @Nullable Class<T> requiredType) {
        if (requiredType != null) {
            if (requiredType.isInstance(request)) {
                return (T) request;
            }
            else if (request instanceof ServletRequestWrapper) {
                return getNativeRequest(((ServletRequestWrapper) request).getRequest(), requiredType);
            }
        }
        return null;
    }
}
