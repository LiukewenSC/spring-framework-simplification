package com.kewen.spring.web.bind.support;

import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.web.bind.WebDataBinder;
import com.kewen.spring.web.context.request.NativeWebRequest;

/**
 * @descrpition 实际上这个类是一个接口，为了简化操作，不做细节处理了
 * @author kewen
 * @since 2023-03-08
 */
public class WebDataBinderFactory {
    public WebDataBinder createBinder(NativeWebRequest webRequest, @Nullable Object target, String objectName)
            throws Exception{
        return new WebDataBinder();
    }
}
