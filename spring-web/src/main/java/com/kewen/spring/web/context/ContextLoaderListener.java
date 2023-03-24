package com.kewen.spring.web.context;

import com.kewen.spring.beans.BeanUtils;
import com.kewen.spring.context.ApplicationContext;
import com.kewen.spring.context.ApplicationContextInitializer;
import com.kewen.spring.context.ApplicationEvent;
import com.kewen.spring.context.ApplicationListener;
import com.kewen.spring.context.event.ContextRefreshedEvent;
import com.kewen.spring.context.event.SourceFilteringListener;
import com.kewen.spring.context.exception.ApplicationContextException;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.core.util.ClassUtils;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.List;

/**
 * spring上下文启动监听器
 */
public class ContextLoaderListener implements ServletContextListener {

    public static final String CONFIG_LOCATION_PARAM = "contextConfigLocation";
    public static final String CONTEXT_ID_PARAM = "contextId";
    public static final String GLOBAL_INITIALIZER_CLASSES_PARAM = "globalInitializerClasses";
    public static final String CONTEXT_INITIALIZER_CLASSES_PARAM = "contextInitializerClasses";
    private final List<ApplicationContextInitializer<WebApplicationContext>> contextInitializers =new ArrayList<>();

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println("ContextLoaderListener.contextInitialized");
        //在此处初始化spring上下文
        initWebApplicationContext(servletContextEvent.getServletContext());
    }

    private WebApplicationContext initWebApplicationContext(ServletContext servletContext){

        //创建上下文对桑
        WebApplicationContext applicationContext = createWebApplicationContext(servletContext);

        if (applicationContext instanceof ConfigurableWebApplicationContext){
            ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext) applicationContext;

            //加载父对象
            ApplicationContext parentContext = loadParentContext(servletContext);
            cwac.setParent(parentContext);

            //配置刷新
            configureAndRefreshWebApplicationContext(cwac,servletContext);
        }



        servletContext.setAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,applicationContext);
        return applicationContext;
    }

    /**
     * 配置刷新
     * @param wac 上下文
     * @param sc servlet上下文
     */
    private void configureAndRefreshWebApplicationContext(ConfigurableWebApplicationContext wac, ServletContext sc) {

        //设置基础的 servlet上下文和配置
        wac.setId(sc.getInitParameter(CONTEXT_ID_PARAM) ==null?"defaultApplicationContextId":sc.getInitParameter(CONTEXT_ID_PARAM));
        wac.setServletContext(sc);
        wac.setConfigLocation(sc.getInitParameter(CONFIG_LOCATION_PARAM));

        //设置初始化器
        customizeContext(sc, wac);

        //todo 这里模拟一个刷新上下文监听器，用来完善监听器流程
        wac.addApplicationListener(new SourceFilteringListener(wac,new ContextRefreshListener()));

        //刷新
        wac.refresh();

    }

    /**
     * 上下文刷新监听器，其实spring自带流程没有这个，SpringMVC中在FrameworkServlet中有此类名的监听器
     * 此处为了模拟监听器流程而设下的，同this.configureAndRefreshWebApplicationContext()方法中的
     *  wac.addApplicationListener(new SourceFilteringListener(wac,new ContextRefreshListener())) 呼应
     */
    public static class ContextRefreshListener implements ApplicationListener<ContextRefreshedEvent> {

        @Override
        public void onApplicationEvent(ContextRefreshedEvent event) {
            ApplicationContext applicationContext = event.getApplicationContext();
            System.out.println(applicationContext);
        }
    }

    private void customizeContext(ServletContext sc, WebApplicationContext wac) {
        //此处设置一些初始化器
        List<Class<ApplicationContextInitializer<WebApplicationContext>>> initializerClasses = determineContextInitializerClasses(sc);
        for (Class<ApplicationContextInitializer<WebApplicationContext>> aClass : initializerClasses) {
            contextInitializers.add(BeanUtils.instantiateClass(aClass));
        }
        //执行初始化
        for (ApplicationContextInitializer<WebApplicationContext> initializer : contextInitializers) {
            initializer.initialize(wac);
        }
    }

    private List<Class<ApplicationContextInitializer<WebApplicationContext>>>  determineContextInitializerClasses(ServletContext sc) {
        ArrayList<Class<ApplicationContextInitializer<WebApplicationContext>>> list = new ArrayList<>();
        String scInitParameter = sc.getInitParameter(GLOBAL_INITIALIZER_CLASSES_PARAM);
        if (scInitParameter != null){
            String[] strings = scInitParameter.split(",");
            for (String className : strings) {
                list.add(loadInitializerClass(className));
            }
        }
        String initParameter = sc.getInitParameter(CONTEXT_INITIALIZER_CLASSES_PARAM);
        if (initParameter != null){
            String[] strings = initParameter.split(",");
            for (String className : strings) {
                list.add(loadInitializerClass(className));
            }
        }
        return list;
    }

    /**
     * 记载初始化监听器
     * @param className
     * @return
     */
    private Class<ApplicationContextInitializer<WebApplicationContext>> loadInitializerClass(String className) {
        try {
            Class<?> clazz = ClassUtils.forName(className);
            if (!ApplicationContextInitializer.class.isAssignableFrom(clazz)) {
                throw new ApplicationContextException(
                        "Initializer class does not implement ApplicationContextInitializer interface: " + clazz);
            }
            return (Class<ApplicationContextInitializer<WebApplicationContext>>) clazz;
        }
        catch (ClassNotFoundException ex) {
            throw new ApplicationContextException("Failed to load context initializer class [" + className + "]", ex);
        }
    }

    protected WebApplicationContext createWebApplicationContext(ServletContext servletContext){
        String contextClassName = servletContext.getInitParameter("contextClass");
        Class<WebApplicationContext> aClass;
        try {
            aClass = ClassUtils.forName(contextClassName);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("applicationContext create failed");
        }

        return BeanUtils.instantiateClass(aClass);
    }
    @Nullable
    protected ApplicationContext loadParentContext(ServletContext servletContext) {
        return null;
    }


    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println("ContextLoaderListener.contextDestroyed");
    }
}
