package com.kewen.spring.web.servlet.mvc.method.annotation;

import cn.hutool.core.annotation.AnnotationUtil;
import com.kewen.spring.beans.BeanUtils;
import com.kewen.spring.beans.exception.BeansException;
import com.kewen.spring.beans.factory.InitializingBean;
import com.kewen.spring.context.ApplicationContext;
import com.kewen.spring.context.ApplicationContextAware;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.http.converter.HttpMessageConverter;
import com.kewen.spring.http.converter.JsonConverter;
import com.kewen.spring.http.converter.StringHttpMessageConverter;
import com.kewen.spring.web.bind.annotation.ControllerAdvice;
import com.kewen.spring.web.context.request.ServletWebRequest;
import com.kewen.spring.web.method.ControllerAdviceBean;
import com.kewen.spring.web.method.HandlerMethod;
import com.kewen.spring.web.method.annotation.ExceptionHandlerMethodResolver;
import com.kewen.spring.web.method.support.HandlerMethodArgumentResolver;
import com.kewen.spring.web.method.support.HandlerMethodArgumentResolverComposite;
import com.kewen.spring.web.method.support.HandlerMethodReturnValueHandler;
import com.kewen.spring.web.method.support.HandlerMethodReturnValueHandlerComposite;
import com.kewen.spring.web.method.support.ModelAndViewContainer;
import com.kewen.spring.web.servlet.HandlerExceptionResolver;
import com.kewen.spring.web.servlet.ModelAndView;
import com.kewen.spring.web.servlet.handler.AbstractHandlerMethodExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @descrpition 异常处理解析器
 * @author kewen
 * @since 2023-03-06
 */
public class ExceptionHandlerExceptionResolver extends AbstractHandlerMethodExceptionResolver implements ApplicationContextAware, InitializingBean {


    @Nullable
    private List<HandlerMethodArgumentResolver> customArgumentResolvers;

    @Nullable
    private HandlerMethodArgumentResolverComposite argumentResolvers;

    @Nullable
    private List<HandlerMethodReturnValueHandler> customReturnValueHandlers;

    @Nullable
    private HandlerMethodReturnValueHandlerComposite returnValueHandlers;

    private List<HttpMessageConverter<?>> messageConverters;


    private final Map<ControllerAdviceBean, ExceptionHandlerMethodResolver> exceptionHandlerAdviceCache =
            new LinkedHashMap<>();

    private final List<ResponseBodyAdvice<Object>> responseBodyAdvice = new ArrayList<>();

    @Nullable
    private ApplicationContext applicationContext;

    public ExceptionHandlerExceptionResolver() {
        this.messageConverters = new ArrayList<>();
        this.messageConverters.add(new StringHttpMessageConverter());
        this.messageConverters.add(new JsonConverter());
    }

    @Override
    protected ModelAndView doResolveHandlerMethodException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod, Exception exception) {
        ServletInvocableHandlerMethod exceptionHandlerMethod = getExceptionHandlerMethod(handlerMethod, exception);
        if (exceptionHandlerMethod == null) {
            return null;
        }
        if (this.argumentResolvers != null) {
            exceptionHandlerMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
        }
        if (this.returnValueHandlers != null) {
            exceptionHandlerMethod.setHandlerMethodReturnValueHandlers(this.returnValueHandlers);
        }

        ServletWebRequest webRequest = new ServletWebRequest(request, response);
        ModelAndViewContainer mavContainer = new ModelAndViewContainer();

        try {
            exceptionHandlerMethod.invokeAndHandle(webRequest, mavContainer, exception, exception.getCause(), handlerMethod);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return null;

    }

    /**
     * 获取异常类对应的HandlerMethod方法
     */
    @Nullable
    protected ServletInvocableHandlerMethod getExceptionHandlerMethod( @Nullable HandlerMethod handlerMethod, Exception exception) {

        Class<?> handlerType = null;

        //如果传入有HandlerMethod，类对应有@ExceptionHandler方法，则直接先解析一次
        if (handlerMethod !=null){
            handlerType = handlerMethod.getBean().getClass();
            ExceptionHandlerMethodResolver resolver= new ExceptionHandlerMethodResolver(handlerType);
            Method method = resolver.resolveMethod(exception);
            if (method != null) {
                return new ServletInvocableHandlerMethod(handlerMethod.getBean(), method);
            }
        }

        //未找到则从@ControllerAdvice对应的类中加载解析器
        for (Map.Entry<ControllerAdviceBean, ExceptionHandlerMethodResolver> entry : this.exceptionHandlerAdviceCache.entrySet()) {
            ControllerAdviceBean advice = entry.getKey();
            if (advice.isApplicableToBeanType(handlerType)) {
                ExceptionHandlerMethodResolver resolver = entry.getValue();
                //解析异常对应的方法
                Method method = resolver.resolveMethod(exception);
                if (method != null) {
                    return new ServletInvocableHandlerMethod(advice.resolveBean(), method);
                }
            }
        }
        return null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        initExceptionHandlerAdviceCache();

        if (this.argumentResolvers == null) {
            List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
            this.argumentResolvers = new HandlerMethodArgumentResolverComposite().addResolvers(resolvers);
        }
        if (this.returnValueHandlers == null) {
            List<HandlerMethodReturnValueHandler> handlers = getDefaultReturnValueHandlers();
            this.returnValueHandlers = new HandlerMethodReturnValueHandlerComposite(handlers);
        }
    }

    private List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandlers() {
        ArrayList<HandlerMethodReturnValueHandler> defaultReturnValueHandlers = new ArrayList<>();
        defaultReturnValueHandlers.add(new RequestResponseBodyMethodProcessor(messageConverters,null,responseBodyAdvice));

        return defaultReturnValueHandlers;
    }

    private void initExceptionHandlerAdviceCache() {
        if (applicationContext == null) {
            return;
        }


        //加载容器中的注解@ControllerAdvice对应的类并组装成ControllerAdviceBean
        List<ControllerAdviceBean> adviceBeans = ControllerAdviceBean.findAnnotatedBeans(applicationContext);

        for (ControllerAdviceBean adviceBean : adviceBeans) {
            Class<?> beanType = adviceBean.getBeanType();
            ExceptionHandlerMethodResolver resolver = new ExceptionHandlerMethodResolver(beanType);
            if (resolver.hasExceptionMappings()) {
                this.exceptionHandlerAdviceCache.put(adviceBean, resolver);
            }
        }

        //添加返回对象增强解析器
        for (ResponseBodyAdvice<Object> value : applicationContext.getBeansOfType(
                ResponseBodyAdvice.class, true, false
        ).values()) {
            responseBodyAdvice.add(value);
        }
    }
    protected List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers() {
        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>();

        //请求参数解析，主要是解析成方法入参对应的Exception
        //目前使用@ExceptionHandler在处理异常的时候由前端=传入，不需要参数解析请求request里面的

        return resolvers;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
}
