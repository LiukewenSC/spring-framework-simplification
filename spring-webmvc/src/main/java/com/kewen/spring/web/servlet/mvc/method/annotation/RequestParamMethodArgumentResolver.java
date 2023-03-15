package com.kewen.spring.web.servlet.mvc.method.annotation;

import com.kewen.spring.beans.BeanUtils;
import com.kewen.spring.beans.factory.ConfigurableBeanFactory;
import com.kewen.spring.core.MethodParameter;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.core.util.StringUtils;
import com.kewen.spring.web.bind.annotation.RequestParam;
import com.kewen.spring.web.bind.annotation.RequestPart;
import com.kewen.spring.web.bind.annotation.ValueConstants;
import com.kewen.spring.web.bind.support.WebDataBinderFactory;
import com.kewen.spring.web.context.request.NativeWebRequest;
import com.kewen.spring.web.method.annotation.AbstractNamedValueMethodArgumentResolver;
import com.kewen.spring.web.method.support.HandlerMethodArgumentResolver;
import com.kewen.spring.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @descrpition param参数请求解析器，解析@RequestParam注解对应的参数
 * @author kewen
 * @since 2023-03-09
 */
public class RequestParamMethodArgumentResolver extends AbstractNamedValueMethodArgumentResolver {

    @Nullable
    private final ConfigurableBeanFactory beanFactory;
    /**
     * 参数解析器数组中有两个此对象，第一个在开始，不使用默认值，第二个使在结尾用默认值作为保底解析
     */
    private final boolean useDefaultResolution;

    public RequestParamMethodArgumentResolver(ConfigurableBeanFactory beanFactory, boolean useDefaultResolution) {
        this.beanFactory = beanFactory;
        this.useDefaultResolution = useDefaultResolution;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        //有@RequestParam先处理
        if (parameter.hasParameterAnnotation(RequestParam.class)) {
            if (Map.class.isAssignableFrom(parameter.nestedIfOptional().getNestedParameterType())) {
                RequestParam requestParam = parameter.getParameterAnnotation(RequestParam.class);
                return (requestParam != null && StringUtils.hasText(requestParam.value()));
            }
            else {
                return true;
            }
        }
        //不处理 @RequestPart
        else {
            if (parameter.hasParameterAnnotation(RequestPart.class)) {
                return false;
            }
            parameter = parameter.nestedIfOptional();
            //使用默认的
            if (this.useDefaultResolution) {
                return BeanUtils.isSimpleProperty(parameter.getNestedParameterType());
            }
            else {
                return false;
            }
        }
    }

    @Override
    protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
        Object arg = null;

        //此处省略了文件上传 MultipartRequest等
        //HttpServletRequest servletRequest = request.getNativeRequest(HttpServletRequest.class);
        //......

        //从请求中获取参数值
        String[] paramValues = request.getParameterValues(name);
        if (paramValues != null) {
            arg = (paramValues.length == 1 ? paramValues[0] : paramValues);
        }
        return arg;
    }

    @Override
    protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
        RequestParam ann = parameter.getParameterAnnotation(RequestParam.class);
        return (ann != null ? new RequestParamNamedValueInfo(ann) : new RequestParamNamedValueInfo());
    }
    private static class RequestParamNamedValueInfo extends NamedValueInfo {

        public RequestParamNamedValueInfo() {
            super("", false, ValueConstants.DEFAULT_NONE);
        }

        public RequestParamNamedValueInfo(RequestParam annotation) {
            super(annotation.value(), annotation.required(), annotation.defaultValue());
        }
    }
}
