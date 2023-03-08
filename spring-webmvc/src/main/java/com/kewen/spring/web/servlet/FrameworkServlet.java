package com.kewen.spring.web.servlet;

import com.kewen.spring.beans.BeanUtils;
import com.kewen.spring.context.ApplicationContext;
import com.kewen.spring.context.ApplicationContextInitializer;
import com.kewen.spring.context.ApplicationListener;
import com.kewen.spring.context.ConfigurableApplicationContext;
import com.kewen.spring.context.event.ContextRefreshedEvent;
import com.kewen.spring.context.exception.ApplicationContextException;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.core.util.ClassUtils;
import com.kewen.spring.core.util.StringUtils;
import com.kewen.spring.web.context.ConfigurableWebApplicationContext;
import com.kewen.spring.web.context.WebApplicationContext;
import com.kewen.spring.web.context.XmlWebApplicationContext;
import com.kewen.spring.web.context.support.WebApplicationContextUtils;
import com.kewen.spring.web.util.NestedServletException;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author kewen
 * @descrpition
 * @since 2023-03-02
 */
public abstract class FrameworkServlet extends HttpServletBean implements ApplicationListener<ContextRefreshedEvent> {

    private Class<?> contextClass = XmlWebApplicationContext.class;

    @Nullable
    private WebApplicationContext webApplicationContext;

    private String contextConfigLocation;

    @Nullable
    private String contextInitializerClasses;

    /**
     * 容器刷新状态的记录
     */
    private volatile boolean refreshEventReceived = false;


    private final List<ApplicationContextInitializer<ConfigurableApplicationContext>> contextInitializers =
            new ArrayList<>();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        this.refreshEventReceived=true;
        onRefresh(event.getApplicationContext());
    }

    @Override
    protected void initServletBean() {

        getServletContext().log("Initializing Spring " + getClass().getSimpleName() + " '" + getServletName() + "'");

        try {
            this.webApplicationContext = initWebApplicationContext();

            initFrameworkServlet();
        }
        catch (Exception ex) {
            System.out.println(("Context initialization failed"+ ex.getMessage()));
            throw ex;
        }

    }

    private void initFrameworkServlet() {

    }

    protected WebApplicationContext initWebApplicationContext() {
        WebApplicationContext rootContext =
                WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        WebApplicationContext wac = null;

        if (wac == null) {
            // No context instance is defined for this servlet -> create a local one
            wac = createWebApplicationContext(rootContext);
        }

        //刷新，子类DespatchServlet实现
        //todo 其实不是从这里刷新的，有一个监听器监听spring容器的刷新事件调起，此处是没有刷新的话再刷新一次，刷新过就不刷新了
        if (!refreshEventReceived){
            //主要用于上下文不支持刷新(无刷新监听)或者已经刷新过了
            onRefresh(wac);
        }

        return wac;
    }

    /**
     * 刷新容器后执行的操作，即初始化9大组件，原框架此处默认实现为空，这里改为抽象类
     * @param wac
     */
    protected abstract void onRefresh(ApplicationContext wac);

    protected WebApplicationContext createWebApplicationContext(ApplicationContext parent) {
        Class<?> contextClass = getContextClass();

        ConfigurableWebApplicationContext wac = (ConfigurableWebApplicationContext) BeanUtils.instantiateClass(contextClass);

        wac.setEnvironment(getEnvironment());
        wac.setParent(parent);
        String configLocation = getContextConfigLocation();
        if (configLocation != null) {
            wac.setConfigLocation(configLocation);
        }
        configureAndRefreshWebApplicationContext(wac);

        return wac;

    }

    private void configureAndRefreshWebApplicationContext(ConfigurableWebApplicationContext wac) {

        //设置id，不判定了，直接走应该有的分支
        wac.setId(ConfigurableWebApplicationContext.APPLICATION_CONTEXT_ID_PREFIX+getServletContext().getContextPath());
        wac.setServletContext(getServletContext());
        wac.setServletConfig(getServletConfig());
        //原MVC框架是组装成PropertyValue并从获取的。此处简化与spring获取的方式保持一致，简单
        wac.setConfigLocation(getServletConfig().getInitParameter("contextConfigLocation"));

        // 这个干嘛的，先不管
        // wac.addApplicationListener(new SourceFilteringListener(wac, new ContextRefreshListener()));

        //钩子函数，可扩展
        postProcessWebApplicationContext(wac);

        //应用初始化器
        applyInitializers(wac);

        //刷新，spring中的通用刷新方法，可不管
        wac.refresh();

    }

    /**
     * 实例化 初始化器并执行 ， 默认没有此行为
     * @param wac
     */
    private void applyInitializers(ConfigurableWebApplicationContext wac) {
        String globalClassNames = getServletContext().getInitParameter("globalInitializerClasses");
        if (globalClassNames != null) {
            for (String className : Objects.requireNonNull(StringUtils.split(globalClassNames, ","))) {
                this.contextInitializers.add(loadInitializer(className, wac));
            }
        }
        if (this.contextInitializerClasses != null) {
            for (String className : Objects.requireNonNull(StringUtils.split(contextInitializerClasses, ","))) {
                this.contextInitializers.add(loadInitializer(className, wac));
            }
        }

        for (ApplicationContextInitializer<ConfigurableApplicationContext> initializer : this.contextInitializers) {
            initializer.initialize(wac);
        }

    }
    private ApplicationContextInitializer<ConfigurableApplicationContext> loadInitializer(
            String className, ConfigurableApplicationContext wac) {
        try {
            Class<?> initializerClass = ClassUtils.forName(className);
            return BeanUtils.instantiateClass(initializerClass, ApplicationContextInitializer.class);
        }
        catch (ClassNotFoundException ex) {
            throw new ApplicationContextException(String.format("Could not load class [%s] specified " +
                    "via 'contextInitializerClasses' init-param", className), ex);
        }
    }

    /**
     * 扩展
     * @param wac
     */
    protected void postProcessWebApplicationContext(ConfigurableWebApplicationContext wac) {
    }

    private Class<?> getContextClass() {
        return this.contextClass;
    }

    public String getContextConfigLocation() {
        return contextConfigLocation;
    }



    /*--------------------------------------执行方法-------------------------------------------*/



    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        processRequest(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.service(req, resp);
    }

    /**
     * 执行请求，可以认为是SpringMVC处理请求的开始方法
     * @param request
     * @param response
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {

        //处理ContextHolder和LocalContext相关的流程，先不管这个，不影响主要流程


        Throwable failureCause = null;
        long startTime = System.currentTimeMillis();

        try {
            doService(request, response);
        } catch (IOException | ServletException e){
            failureCause = e;
            throw e;
        }catch (Exception e) {
            failureCause=e;
            throw new NestedServletException("Request processing failed",e);
        } finally {

            // logResult(request, response, failureCause, asyncManager);
            //发布事件，先不管
            publishRequestHandledEvent(request, response, startTime, failureCause);
        }


    }

    /**
     * 发布事件，先不管
     */
    private void publishRequestHandledEvent(HttpServletRequest request, HttpServletResponse response,
                                            long startTime, @Nullable Throwable failureCause) {
        //todo 发布事件
    }

    /**
     * 执行服务，由子类实现，具体执行的流程
     */
    protected abstract void doService(HttpServletRequest request, HttpServletResponse response)
            throws Exception;
}
