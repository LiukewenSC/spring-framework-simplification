package com.kewen.spring.web.servlet;

import cn.hutool.setting.dialect.PropsUtil;
import com.kewen.spring.beans.BeanUtils;
import com.kewen.spring.beans.factory.BeanFactoryUtils;
import com.kewen.spring.context.ApplicationContext;
import com.kewen.spring.core.io.ClassPathResource;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.core.util.ClassUtils;


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
                    Class<T> name = ClassUtils.forName(str);
                    T instance = BeanUtils.instantiateClass(name);
                    // TODO: 2023/3/6 需要添加实例到工厂中
                    ts.add(instance);
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
}
