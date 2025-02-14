package com.kewen.spring.web.servlet.mvc.method.annotation;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.lang.hash.Hash;
import com.kewen.spring.beans.exception.BeansException;
import com.kewen.spring.beans.factory.InitializingBean;
import com.kewen.spring.context.ApplicationContext;
import com.kewen.spring.context.ApplicationContextAware;
import com.kewen.spring.context.annotation.Controller;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.web.bind.annotation.RequestMapping;
import com.kewen.spring.web.method.HandlerMethod;
import com.kewen.spring.web.servlet.HandlerExecutionChain;
import com.kewen.spring.web.servlet.HandlerInterceptor;
import com.kewen.spring.web.servlet.HandlerMapping;
import com.kewen.spring.web.servlet.handler.AbstractHandlerMapping;
import com.kewen.spring.web.servlet.mvc.condition.PatternsRequestCondition;
import com.kewen.spring.web.servlet.mvc.method.RequestMappingInfo;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @descrpition 内部维护了controller方法的封装HandlerMethod对应的url请求连接，并在初始化组件阶段做好初始化的映射
 * @author kewen
 * @since 2023-03-06
 */
public class RequestMappingHandlerMapping extends AbstractHandlerMapping implements ApplicationContextAware,InitializingBean {

    ApplicationContext applicationContext;
    private final MappingRegistry mappingRegistry = new MappingRegistry();
    @Override
    public void afterPropertiesSet() throws Exception {
        initHandlerMethods();
    }
    protected void initHandlerMethods() {
        Map<String, Object> beansOfType = obtainApplicationContext().getBeansOfType(Object.class, true, false);
        for (Object bean : beansOfType.values()) {
            if (isHandler(bean.getClass())) {
                detectHandlerMethods(bean);
            }
        }
    }

    private void detectHandlerMethods(Object handler) {
        Class<?> handlerType = handler.getClass();

        //获取控制器对应的方法 ,改动很大
        Map<Method, RequestMappingInfo> methods = selectMethods(handlerType);

        methods.forEach((method, requestMappingInfo) -> {
            //注册控制器方法
            registerHandlerMethod(handler, method, requestMappingInfo);
        });


    }
    private void registerHandlerMethod(Object handler, Method method, RequestMappingInfo requestMappingInfo) {

        this.mappingRegistry.register(requestMappingInfo, handler, method);

        updateConsumesCondition(requestMappingInfo,method);
    }

    /**
     *  // TODO: 2023/3/7
     * 有一个requestBody的处理，目前不晓得是啥意思，等用到的时候再说
     */
    private void updateConsumesCondition(RequestMappingInfo info, Method method) {
        /*ConsumesRequestCondition condition = info.getConsumesCondition();
        if (!condition.isEmpty()) {
            for (Parameter parameter : method.getParameters()) {
                MergedAnnotation<RequestBody> annot = MergedAnnotations.from(parameter).get(RequestBody.class);
                if (annot.isPresent()) {
                    condition.setBodyRequired(annot.getBoolean("required"));
                    break;
                }
            }
        }*/
    }

    /**
     * 获取控制器对应的方法。
     * 此处改了大量的逻辑，原框架十分复杂，最终结果是得到方法对应的映射
     * @param clazz
     * @return
     */
    private Map<Method, RequestMappingInfo> selectMethods( Class<?> clazz ){
        String prefix="";
        RequestMapping requestMapping = clazz.getAnnotation(RequestMapping.class);
        if (requestMapping !=null){
            prefix = requestMapping.value();
        }
        Map<Method, RequestMappingInfo> map = new HashMap<>(clazz.getMethods().length);
        for (Method method : clazz.getMethods()) {
            if (method.isAnnotationPresent(RequestMapping.class)){
                String value = method.getAnnotation(RequestMapping.class).value();
                RequestMappingInfo info = new RequestMappingInfo(
                        clazz.getName(),
                        new PatternsRequestCondition(prefix + value)
                );
                map.put(method,info);
            }
        }
        return map;
    }

    /**
     * 判断是否是Controller对应的控制器
     * @param beanClass
     * @return
     */
    private boolean isHandler(Class beanClass){
        List<Annotation> annotations = AnnotationUtil.scanClass(beanClass);
        for (Annotation annotation : annotations) {
            boolean hasControllerLoop = hasControllerLoop(annotation);
            if (hasControllerLoop){
                return true;
            }
        }
        return false;
    }
    private boolean hasControllerLoop(Annotation annotation){
        if (annotation.annotationType()== Controller.class){
            return true;
        }
        List<Annotation> metaAnnotation = AnnotationUtil.scanMetaAnnotation(annotation.annotationType());
        for (Annotation sub : metaAnnotation) {
            boolean hasController = hasControllerLoop(sub);
            if (hasController){
                return true;
            }
        }
        return false;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
        //todo 从标签中注入进来的，这里简化以下直接从容器中带进来
        Map<String, HandlerInterceptor> beansOfType = applicationContext.getBeansOfType(HandlerInterceptor.class, true, false);
        if (beansOfType !=null){
            this.adaptedInterceptors.addAll(beansOfType.values());
        }
    }


    private class MappingRegistry {
        private final Map<RequestMappingInfo, HandlerMethod> mappingLookup = new LinkedHashMap<>();
        private final HashMap<String, RequestMappingInfo> urlLookup = new LinkedHashMap();
        public void register(RequestMappingInfo mapping, Object handler, Method method) {

            //添加 HandlerMethod
            HandlerMethod handlerMethod = createHandlerMethod(handler, method);
            mappingLookup.put(mapping,handlerMethod);

            //添加urlLookup
            List<String> directUrls = getDirectUrls(mapping);
            for (String url : directUrls) {
                this.urlLookup.put(url, mapping);
            }
            //后面还有一些，不弄了，不晓得在搞啥
        }
        private List<String> getDirectUrls(RequestMappingInfo mapping) {
            return new ArrayList<>(mapping.getPatternsCondition().getPatterns());
        }
        private Map<RequestMappingInfo, HandlerMethod> getMappings(){
            return mappingLookup;
        }
        @Nullable
        public RequestMappingInfo getMappingsByUrl(String urlPath) {
            return this.urlLookup.get(urlPath);
        }

    }
    protected HandlerMethod createHandlerMethod(Object handler, Method method) {
        return new HandlerMethod(handler, method);
    }

    private ApplicationContext obtainApplicationContext() {
        return applicationContext;
    }







    @Override
    protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
        //原框架又抽象了几个类，不管了，就这样
        String lookupPath = request.getRequestURI();
        HandlerMethod handlerMethod = lookupHandlerMethod(lookupPath, request);
        //原框架有处理HandlerMethod保存的对象为string的情况，暂不考虑
        return handlerMethod;
    }

    /**
     * 找到匹配的处理方法，原框架有复杂的逻辑处理并找取最优的，这里直接单个处理了
     * 同时从 mappingRegistry Map<RequestMappingInfo, HandlerMethod>  中获取HandlerMethod也简化了
     * @return
     */
    private HandlerMethod lookupHandlerMethod(String lookupPath, HttpServletRequest request) {
        RequestMappingInfo mappingsByUrl = mappingRegistry.getMappingsByUrl(lookupPath);
        HandlerMethod handlerMethod = mappingRegistry.getMappings().get(mappingsByUrl);
        return handlerMethod;


    }


}
