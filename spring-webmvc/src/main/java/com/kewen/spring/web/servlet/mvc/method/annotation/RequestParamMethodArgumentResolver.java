package com.kewen.spring.web.servlet.mvc.method.annotation;

import com.kewen.spring.core.MethodParameter;
import com.kewen.spring.web.bind.support.WebDataBinderFactory;
import com.kewen.spring.web.context.request.NativeWebRequest;
import com.kewen.spring.web.method.support.HandlerMethodArgumentResolver;
import com.kewen.spring.web.method.support.ModelAndViewContainer;

import java.util.Map;

/**
 * @descrpition param参数请求解析器
 * @author kewen
 * @since 2023-03-09
 */
public class RequestParamMethodArgumentResolver implements HandlerMethodArgumentResolver {


    /**
     * 参数解析器数组中有两个此对象，第一个在开始，不使用默认值，第二个使在结尾用默认值
     */
    private final boolean useDefaultResolution;

    public RequestParamMethodArgumentResolver(boolean useDefaultResolution) {
        this.useDefaultResolution = useDefaultResolution;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (parameter.hasParameterAnnotation(RequestParam.class)) {
            if (Map.class.isAssignableFrom(parameter.nestedIfOptional().getNestedParameterType())) {
                RequestParam requestParam = parameter.getParameterAnnotation(RequestParam.class);
                return (requestParam != null && StringUtils.hasText(requestParam.name()));
            }
            else {
                return true;
            }
        }
        else {
            if (parameter.hasParameterAnnotation(RequestPart.class)) {
                return false;
            }
            parameter = parameter.nestedIfOptional();
            if (MultipartResolutionDelegate.isMultipartArgument(parameter)) {
                return true;
            }
            else if (this.useDefaultResolution) {
                return BeanUtils.isSimpleProperty(parameter.getNestedParameterType());
            }
            else {
                return false;
            }
        }
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        return null;
    }
}
