package com.kewen.spring.web.method.support;

import com.kewen.spring.core.MethodParameter;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.web.bind.support.WebDataBinderFactory;
import com.kewen.spring.web.context.request.NativeWebRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @descrpition 组合的参数解析器
 * @author kewen
 * @since 2023-03-08
 */
public class HandlerMethodArgumentResolverComposite implements HandlerMethodArgumentResolver{

    private final List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();
    private final Map<MethodParameter, HandlerMethodArgumentResolver> argumentResolverCache =
            new ConcurrentHashMap<>(256);


    public HandlerMethodArgumentResolverComposite addResolver(HandlerMethodArgumentResolver resolver) {
        this.argumentResolvers.add(resolver);
        return this;
    }
    public HandlerMethodArgumentResolverComposite addResolvers(@Nullable List<? extends HandlerMethodArgumentResolver> resolvers) {

        if (resolvers != null) {
            this.argumentResolvers.addAll(resolvers);
        }
        return this;
    }

    public List<HandlerMethodArgumentResolver> getResolvers() {
        return Collections.unmodifiableList(this.argumentResolvers);
    }

    public void clear() {
        this.argumentResolvers.clear();
    }

    /**
     * 获取参数MethodParameter对应的参数解析器
     * @param parameter
     * @return
     */
    @Nullable
    private HandlerMethodArgumentResolver getArgumentResolver(MethodParameter parameter) {
        HandlerMethodArgumentResolver result = this.argumentResolverCache.get(parameter);
        if (result == null) {
            for (HandlerMethodArgumentResolver resolver : this.argumentResolvers) {
                if (resolver.supportsParameter(parameter)) {
                    result = resolver;
                    this.argumentResolverCache.put(parameter, result);
                    break;
                }
            }
        }
        return result;
    }



    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return getArgumentResolver(parameter) != null;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HandlerMethodArgumentResolver argumentResolver = getArgumentResolver(parameter);
        return argumentResolver.resolveArgument(parameter, mavContainer, webRequest, binderFactory);
    }
}
