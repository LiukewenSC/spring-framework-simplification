package com.kewen.spring.web.servlet;

import cn.hutool.setting.dialect.PropsUtil;
import com.kewen.spring.beans.BeanUtils;
import com.kewen.spring.beans.factory.BeanFactoryUtils;
import com.kewen.spring.context.ApplicationContext;
import com.kewen.spring.core.io.ClassPathResource;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.core.util.ClassUtils;
import com.kewen.spring.web.util.NestedServletException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * @descrpition springmvc的入口
 * @author kewen
 * @since 2023-03-02
 */
public class DispatcherServlet extends FrameworkServlet {
    public static final String PAGE_NOT_FOUND_LOG_CATEGORY = "org.springframework.web.servlet.PageNotFound";

    /**
     * Name of the class path resource (relative to the DispatcherServlet class)
     * that defines DispatcherServlet's default strategy names.
     */
    private static final String DEFAULT_STRATEGIES_PATH = "DispatcherServlet.properties";

    /**
     * Common prefix that DispatcherServlet's default strategy attributes start with.
     */
    private static final String DEFAULT_STRATEGIES_PREFIX = "org.springframework.web.servlet";


    @Nullable
    private List<HandlerMapping> handlerMappings;
    @Nullable
    private List<HandlerAdapter> handlerAdapters;
    @Nullable
    private List<HandlerExceptionResolver> handlerExceptionResolvers;

    private static final Properties defaultStrategies;

    static {
        try {
            ClassPathResource resource = new ClassPathResource(DEFAULT_STRATEGIES_PATH,null, DispatcherServlet.class);
            Properties properties = new Properties();
            properties.load(resource.getInputStream());
            defaultStrategies = properties;
        }
        catch (IOException ex) {
            throw new IllegalStateException("Could not load '" + DEFAULT_STRATEGIES_PATH + "': " + ex.getMessage());
        }
    }


    public DispatcherServlet() {
        System.out.println("创建DispatcherServlet");
    }

    /**
     * //初始化9大组件
     * @param wac
     */
    @Override
    protected void onRefresh(ApplicationContext wac) {
        initStrategies(wac);
    }


    protected void initStrategies(ApplicationContext context) {
        //文件上传组件，先不管
        initMultipartResolver(context);
        //本地语言组件，先不管
        initLocaleResolver(context);
        //主题解析器，先不管
        initThemeResolver(context);
        //映射处理器
        initHandlerMappings(context);
        //适配器处理器
        initHandlerAdapters(context);
        //异常处理器
        initHandlerExceptionResolvers(context);
        //请求转换，先不管
        initRequestToViewNameTranslator(context);
        //视图解析器，先不管，目标可以解析出json就可以
        initViewResolvers(context);
        //flash管理器，先不管
        initFlashMapManager(context);
    }

    private void initMultipartResolver(ApplicationContext context) {

    }

    private void initLocaleResolver(ApplicationContext context) {

    }


    private void initThemeResolver(ApplicationContext context) {

    }

    /**
     * 请求映射器
     * @param context
     */
    private void initHandlerMappings(ApplicationContext context) {
        this.handlerMappings=null;
        Map<String, HandlerMapping> handlerMappingMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerMapping.class, true, false);
        handlerMappings=new ArrayList<>();
        if (handlerMappingMap!=null && !handlerMappingMap.isEmpty()){
            handlerMappings.addAll(handlerMappingMap.values());
        } else {
            handlerMappings.addAll(getDefaultStrategies(context, HandlerMapping.class));
        }
    }

    /**
     * 根据类型获取默认的处理器
     * @param context
     * @param strategyInterface
     * @param <T>
     * @return
     */
    protected <T> List<T> getDefaultStrategies(ApplicationContext context, Class<T> strategyInterface) {
        String key = strategyInterface.getName();
        String value = defaultStrategies.getProperty(key);
        ArrayList<T> ts = new ArrayList<>();
        if (value !=null){
            String[] strs = value.split(",");
            for (String str : strs) {
                try {
                    Class<T> beanClass = ClassUtils.forName(str);
                    T bean = context.getAutowireCapableBeanFactory().createBean(beanClass);
                    ts.add(bean);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return ts;
    }

    private void initHandlerAdapters(ApplicationContext context) {
        this.handlerAdapters=null;
        Map<String, HandlerAdapter> handlerAdapterMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerAdapter.class, true, false);
        handlerAdapters=new ArrayList<>();
        if (handlerAdapterMap!=null && !handlerAdapterMap.isEmpty()){
            handlerAdapters.addAll(handlerAdapterMap.values());
        } else {
            handlerAdapters.addAll(getDefaultStrategies(context, HandlerAdapter.class));
        }
    }

    private void initHandlerExceptionResolvers(ApplicationContext context) {
        this.handlerExceptionResolvers=null;
        Map<String, HandlerExceptionResolver> handlerExceptionMap = BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerExceptionResolver.class, true, false);
        handlerExceptionResolvers=new ArrayList<>();
        if (handlerExceptionMap!=null && !handlerExceptionMap.isEmpty()){
            handlerExceptionResolvers.addAll(handlerExceptionMap.values());
        } else {
            handlerExceptionResolvers.addAll(getDefaultStrategies(context, HandlerExceptionResolver.class));
        }
    }

    private void initRequestToViewNameTranslator(ApplicationContext context) {

    }

    private void initViewResolvers(ApplicationContext context) {

    }

    private void initFlashMapManager(ApplicationContext context) {

    }



    /*--------------------------------------执行方法-------------------------------------------*/




    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //记录请求，先不管
        logRequest(request);

        //设置request属性，方便后续在request上拿去，先不管


        //
        try {
            doDispatch(request,response);
        } finally {
            //重置Request中的请求，先不=管
        }
    }
    private void logRequest(HttpServletRequest request) {

    }

    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HandlerExecutionChain mappedHandler = null;

        ModelAndView mv = null;
        Exception dispatchException = null;
        try {
            try {
                mappedHandler = getHandler(request);

                if (mappedHandler==null){
                    //直接返回了
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                HandlerAdapter ha = getHandlerAdapter(mappedHandler.getHandler());


                //对于GET和HEAD请求，此处做了一次查询缓存，先不管


                //执行拦截器前置处理，执行pre方法，未通过则结束
                if (!mappedHandler.applyPreHandle(request, response)) {
                    return;
                }


                //执行请求方法
                mv = ha.handle(request, response, mappedHandler.getHandler());



                //执行拦截器后置处理
                mappedHandler.applyPostHandle(request, response, mv);




            } catch (Exception e) {
                dispatchException = e;
            }catch (Throwable t) {
                dispatchException = new NestedServletException("Handler dispatch failed", t);
            }
            //处理结果，包括异常结果和正常结果，前后端分离实际上就只剩下异常处理了
            processDispatchResult(request, response, mappedHandler, mv, dispatchException);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //简化处理，直接在finally中执行，原框架是分别在几个地方处理的，干嘛那么麻烦呢
            mappedHandler.triggerAfterCompletion(request,response,dispatchException);
        }

    }

    private void processDispatchResult(HttpServletRequest request, HttpServletResponse response,
           HandlerExecutionChain mappedHandler, ModelAndView mv, Exception exception) throws Exception {

        Object handler = (mappedHandler != null ? mappedHandler.getHandler() : null);
        mv = processHandlerException(request, response, handler, exception);

    }

    @Nullable
    protected ModelAndView processHandlerException(HttpServletRequest request, HttpServletResponse response,
                                                   @Nullable Object handler, Exception ex) throws Exception {
        ModelAndView exMv = null;

        //没有异常则直接返回了
        if (ex == null){
            return exMv;
        }
        if (this.handlerExceptionResolvers != null) {
            for (HandlerExceptionResolver resolver : this.handlerExceptionResolvers) {
                exMv = resolver.resolveException(request, response, handler, ex);
                if (exMv != null) {
                    break;
                }
            }
        }
        return exMv;
    }

    @Nullable
    protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        if (this.handlerMappings != null) {
            for (HandlerMapping mapping : this.handlerMappings) {
                HandlerExecutionChain handler = mapping.getHandler(request);
                if (handler != null) {
                    return handler;
                }
            }
        }
        return null;
    }
    protected HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
        if (this.handlerAdapters != null) {
            for (HandlerAdapter adapter : this.handlerAdapters) {
                if (adapter.supports(handler)) {
                    return adapter;
                }
            }
        }
        throw new ServletException("No adapter for handler [" + handler +
                "]: The DispatcherServlet configuration needs to include a HandlerAdapter that supports this handler");
    }
}
