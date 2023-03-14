package com.kewen.spring.web.servlet.mvc.method.annotation;

import com.kewen.spring.beans.BeanUtils;
import com.kewen.spring.beans.BeanWrapperImpl;
import com.kewen.spring.beans.PropertyValue;
import com.kewen.spring.core.MethodParameter;
import com.kewen.spring.core.util.Assert;
import com.kewen.spring.web.bind.WebDataBinder;
import com.kewen.spring.web.bind.annotation.ModelAttribute;
import com.kewen.spring.web.bind.support.WebDataBinderFactory;
import com.kewen.spring.web.context.request.NativeWebRequest;
import com.kewen.spring.web.method.support.HandlerMethodArgumentResolver;
import com.kewen.spring.web.method.support.ModelAndViewContainer;

import java.beans.PropertyDescriptor;

/**
 * @descrpition
 * @author kewen
 * @since 2023-03-14
 */
public class ServletModelAttributeMethodProcessor implements HandlerMethodArgumentResolver {

    /**
     * 是否不需要注解
     */
    private final boolean annotationNotRequired;

    public ServletModelAttributeMethodProcessor(boolean annotationNotRequired) {
        this.annotationNotRequired = annotationNotRequired;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return (parameter.hasParameterAnnotation(ModelAttribute.class) ||
                (this.annotationNotRequired && !BeanUtils.isSimpleProperty(parameter.getParameterType())));
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        Assert.state(mavContainer != null, "ModelAttributeMethodProcessor requires ModelAndViewContainer");
        Assert.state(binderFactory != null, "ModelAttributeMethodProcessor requires WebDataBinderFactory");

        //处理注解为ModelAttribute
        ModelAttribute ann = parameter.getParameterAnnotation(ModelAttribute.class);
        if (ann != null) {
            String name = ann.value();
            mavContainer.setBinding(name, ann.binding());
        }

        //简化处理了，
        // 原框架构造了 WebDataBinder ，并通过构造PropertyValue来处理加入数据，
        // 这里利用jdk的PropertyDescriptor 简单处理


        WebDataBinder binder = binderFactory.createBinder(webRequest, null, null);

        Class<?> parameterType = parameter.getParameterType();
        Object instance = parameterType.newInstance();
        BeanWrapperImpl beanWrapper = new BeanWrapperImpl(instance);
        PropertyDescriptor[] descriptors = beanWrapper.getPropertyDescriptors();
        for (PropertyDescriptor descriptor : descriptors) {
            String name = descriptor.getName();
            String[] values = webRequest.getParameterValues(name);
            if (values !=null && values.length>0){
                Object arg = null;
                if (values.length==1){
                    arg = values[0];
                } else {
                    arg = values;
                }
                Object convert = binder.convertIfNecessary(arg, descriptor.getPropertyType(), null);
                descriptor.getWriteMethod().invoke(instance, convert);
            }
        }
        return instance;

    }
}
