package com.kewen.spring.web.method.support;

import com.kewen.spring.core.MethodParameter;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.web.bind.support.WebDataBinderFactory;
import com.kewen.spring.web.context.request.NativeWebRequest;

/**
 * @descrpition 参数解析器
 * @author kewen
 * @since 2023-03-08
 */
public interface HandlerMethodArgumentResolver {
    boolean supportsParameter(MethodParameter parameter);

    @Nullable
    Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
                           NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception;

}
