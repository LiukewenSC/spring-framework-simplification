package com.kewen.spring.web.servlet.mvc.method.annotation;

import com.kewen.spring.beans.exception.BeansException;
import com.kewen.spring.beans.factory.BeanFactory;
import com.kewen.spring.beans.factory.BeanFactoryAware;
import com.kewen.spring.beans.factory.ConfigurableBeanFactory;
import com.kewen.spring.beans.factory.InitializingBean;
import com.kewen.spring.context.ApplicationContext;
import com.kewen.spring.context.ApplicationContextAware;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.http.converter.HttpMessageConverter;
import com.kewen.spring.http.converter.StringHttpMessageConverter;
import com.kewen.spring.web.bind.support.WebDataBinderFactory;
import com.kewen.spring.web.context.request.ServletWebRequest;
import com.kewen.spring.web.method.HandlerMethod;
import com.kewen.spring.web.method.support.HandlerMethodArgumentResolver;
import com.kewen.spring.web.method.support.HandlerMethodArgumentResolverComposite;
import com.kewen.spring.web.method.support.HandlerMethodReturnValueHandler;
import com.kewen.spring.web.method.support.HandlerMethodReturnValueHandlerComposite;
import com.kewen.spring.web.method.support.ModelAndViewContainer;
import com.kewen.spring.web.servlet.HandlerAdapter;
import com.kewen.spring.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author kewen
 * @descrpition
 * @since 2023-03-06
 */
public class RequestMappingHandlerAdapter implements HandlerAdapter, BeanFactoryAware,ApplicationContextAware, InitializingBean {
    @Nullable
    private ApplicationContext applicationContext;
    @Nullable
    private ConfigurableBeanFactory beanFactory;
    @Nullable
    private HandlerMethodArgumentResolverComposite argumentResolvers;
    @Nullable
    private HandlerMethodReturnValueHandlerComposite returnValueHandlers;

    private List<HttpMessageConverter<?>> messageConverters;

    private final  List<RequestBodyAdvice<Object>> requestBodyAdvices = new ArrayList<>();
    private final  List<ResponseBodyAdvice<Object>> responseBodyAdvices = new ArrayList<>();

    @Nullable
    WebDataBinderFactory dataBinderFactory;
    /**
     * 获取默认的参数解析器
     * @return
     */
    private List<HandlerMethodArgumentResolver> getDefaultArgumentResolvers() {

        List<HandlerMethodArgumentResolver> resolvers = new ArrayList<>(30);

        resolvers.add(new RequestParamMethodArgumentResolver(beanFactory, false));

        //还有很多默认的，后续添加进来

        resolvers.add(new RequestParamMethodArgumentResolver(beanFactory, true));
        return resolvers;
    }
    private List<HandlerMethodReturnValueHandler> getDefaultReturnValueHandler(){
        List<HandlerMethodReturnValueHandler> returnValueHandlers = new ArrayList<>();

        //@ResponseBody处理，这里稍微和原框架不一样，原框架是把 requestBodyAdvice 和 responseBodyAdvice组合在一起利用List<Object>传入再判定返回,现在就分开，要明确一点
        returnValueHandlers.add(new RequestResponseBodyMethodProcessor(messageConverters,requestBodyAdvices,responseBodyAdvices));
        return returnValueHandlers;
    }

    @Override
    public boolean supports(Object handler) {
        return (handler instanceof HandlerMethod);
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return handleInternal(request,response,(HandlerMethod)handler);
    }
    protected ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {
        ModelAndView mav;
        checkRequest(request);

        //有session的其他判定分支，不管，只保留基本的分支
        mav = invokeHandlerMethod(request, response, handlerMethod);

        // 此处根据请求做一些缓存，先不做

        return mav;
    }
    /**
     * 检查给定请求中所支持的方法和所需的会话(如果有的话)  先不做
     */
    private void checkRequest(HttpServletRequest request) {

    }

    /**
     * 这里面就很重要了，有关于请求和返回组装的大量的逻辑，先不按照官方走，先调起来controller再说
     * @return
     */
    private ModelAndView invokeHandlerMethod1(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) {
        Object controller = handlerMethod.getBean();
        Method method = handlerMethod.getMethod();
        try {
            Object invoke = method.invoke(controller);
            System.out.println(invoke);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }
    private ModelAndView invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) throws Exception {

        //创建ServletWebRequest对象
        ServletWebRequest webRequest = new ServletWebRequest(request, response);

        // 创建ServletInvocableHandlerMethod，此类主要是合并了一些数据并执行方法invokeAndHandle
        ServletInvocableHandlerMethod invocableMethod = new ServletInvocableHandlerMethod(handlerMethod);

        //加入参数解析器
        if (this.argumentResolvers != null) {
            invocableMethod.setHandlerMethodArgumentResolvers(this.argumentResolvers);
        }
        //返回值解析器
        if (this.returnValueHandlers != null) {
            invocableMethod.setHandlerMethodReturnValueHandlers(this.returnValueHandlers);
        }
        if (this.dataBinderFactory !=null){
            invocableMethod.setDataBinderFactory(this.dataBinderFactory);
        }
        ModelAndViewContainer mavContainer = new ModelAndViewContainer();

        //执行方法
        invocableMethod.invokeAndHandle(webRequest, mavContainer);

        return null;

    }


    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        if (beanFactory instanceof ConfigurableBeanFactory){
            this.beanFactory= ((ConfigurableBeanFactory) beanFactory);
        }
    }
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }
    @Override
    public void afterPropertiesSet() throws Exception {
        //初始化原有的
        initControllerAdviceCache();

        //返回值解析器
        this.messageConverters=getDefaultMessageConverters();

        List<HandlerMethodArgumentResolver> defaultArgumentResolvers = getDefaultArgumentResolvers();
        HandlerMethodArgumentResolverComposite composite = new HandlerMethodArgumentResolverComposite();
        composite.addResolvers(defaultArgumentResolvers);
        this.argumentResolvers=composite;

        //此处简化了操作，原逻辑并非在这里
        this.dataBinderFactory=new WebDataBinderFactory();
        //返回值解析前的处理器
        this.returnValueHandlers=new HandlerMethodReturnValueHandlerComposite(getDefaultReturnValueHandler());
    }

    private List<HttpMessageConverter<?>> getDefaultMessageConverters() {
        return Arrays.asList(new StringHttpMessageConverter());
    }

    private void initControllerAdviceCache() {

        //获取对应的ResponseBodyAdvance实现，先凑活着用，后面再改
        Map<String, ResponseBodyAdvice> beansOfType = applicationContext.getBeansOfType(ResponseBodyAdvice.class, true, false);
        if (beansOfType !=null){
            Collection<ResponseBodyAdvice> values = beansOfType.values();
            for (ResponseBodyAdvice value : values) {
                this.responseBodyAdvices.add(value);
            }
        }
    }

}
