package com.kewen.spring.web.method.annotation;

import com.kewen.spring.core.MethodParameter;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.web.bind.ServletRequestBindingException;
import com.kewen.spring.web.bind.WebDataBinder;
import com.kewen.spring.web.bind.annotation.ValueConstants;
import com.kewen.spring.web.bind.support.WebDataBinderFactory;
import com.kewen.spring.web.context.request.NativeWebRequest;
import com.kewen.spring.web.method.support.HandlerMethodArgumentResolver;
import com.kewen.spring.web.method.support.ModelAndViewContainer;
import com.kewen.spring.web.util.NestedServletException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @descrpition 抽象的 键值对解析器
 * @author kewen
 * @since 2023-03-09
 */
public abstract class AbstractNamedValueMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final Map<MethodParameter, NamedValueInfo> namedValueInfoCache = new ConcurrentHashMap<>(256);

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        //这里原框架就有些复杂了，构造对象，解析名称，在执行解析
        //实际上就是从Request请求中拿到parameter对应的值就行了，如parameter的@RequestParam的value值对应的request中的值....过于复杂

        //获取NameValue值
        NamedValueInfo namedValueInfo = getNamedValueInfo(parameter);
        MethodParameter nestedParameter = parameter.nestedIfOptional();
        //解析
        Object resolvedName = resolveEmbeddedValuesAndExpressions(namedValueInfo.name);

        //从请求中获取参数值
        Object arg = resolveName(resolvedName.toString(), nestedParameter, webRequest);

        //校验参数值，先简单处理
        if (arg==null){
            //校验必传性
            if (namedValueInfo.required ) {
                throw new ServletRequestBindingException("参数异常");
            }
        }

        //转换类型，如String转换为Integer
        if (binderFactory !=null){
            WebDataBinder binder = binderFactory.createBinder(webRequest, null, namedValueInfo.name);
            arg = binder.convertIfNecessary(arg, parameter.getParameterType(), parameter);
        }


        //继续处理值，目前只有PathViraible会覆写此方法，其他的用不上
        handleResolvedValue(arg, namedValueInfo.name, parameter, mavContainer, webRequest);


        return arg;
    }
    protected void handleResolvedValue(@Nullable Object arg, String name, MethodParameter parameter,
                                       @Nullable ModelAndViewContainer mavContainer, NativeWebRequest webRequest) {
    }

    /**
     * 解析名字，由子类实现
     */
    protected abstract Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception;

    /**
     * 解析占位符等，目前不管这个，
     * @param name
     * @return
     */
    private Object resolveEmbeddedValuesAndExpressions(String name) {
        //todo 解析占位符，不管先
        return name;
    }

    private NamedValueInfo getNamedValueInfo(MethodParameter parameter) {
        NamedValueInfo namedValueInfo = this.namedValueInfoCache.get(parameter);
        if (namedValueInfo == null) {
            namedValueInfo = createNamedValueInfo(parameter);
            //这里要去通过反射拿参数名，通过asm实现，算了
            namedValueInfo = updateNamedValueInfo(parameter, namedValueInfo);
            this.namedValueInfoCache.put(parameter, namedValueInfo);
        }
        return namedValueInfo;
    }

    /**
     * 更新值，有点绕来绕去的感觉了，可以不这样的
     */
    private NamedValueInfo updateNamedValueInfo(MethodParameter parameter, NamedValueInfo info) {
        String name = info.name;
        if (info.name.isEmpty()) {
            parameter.getParameterName();
            if (name == null) {
                throw new IllegalArgumentException(
                        "Name for argument of type [" + parameter.getNestedParameterType().getName() +
                                "] not specified, and parameter name information not found in class file either.");
            }
        }
        String defaultValue = (ValueConstants.DEFAULT_NONE.equals(info.defaultValue) ? null : info.defaultValue);
        return new NamedValueInfo(name, info.required, defaultValue);
    }
    /**
     * 创建key-value对，由子类实现
     * @param parameter
     * @return
     */
    protected abstract NamedValueInfo createNamedValueInfo(MethodParameter parameter);
    protected static class NamedValueInfo {
        private final String name;
        private final boolean required;
        @Nullable
        private final String defaultValue;
        public NamedValueInfo(String name, boolean required, @Nullable String defaultValue) {
            this.name = name;
            this.required = required;
            this.defaultValue = defaultValue;
        }
    }
}
