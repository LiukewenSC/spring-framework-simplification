package com.kewen.spring.web.servlet.mvc.method.annotation;

import com.kewen.spring.core.MethodParameter;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.web.bind.support.WebDataBinderFactory;
import com.kewen.spring.web.context.request.NativeWebRequest;
import com.kewen.spring.web.context.request.ServletWebRequest;
import com.kewen.spring.web.method.HandlerMethod;
import com.kewen.spring.web.method.support.HandlerMethodArgumentResolverComposite;
import com.kewen.spring.web.method.support.HandlerMethodReturnValueHandlerComposite;
import com.kewen.spring.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Method;

/**
 * @descrpition 封装请求的HandlerMethod并配置初始值到其中，后续调用反射执行其方法
 *      此类主要用于执行请求
 * @author kewen
 * @since 2023-03-08
 */
public class ServletInvocableHandlerMethod extends HandlerMethod {

    /**
     * 参数解析器
     */
    private HandlerMethodArgumentResolverComposite resolvers = new HandlerMethodArgumentResolverComposite();

    /**
     * 返回值解析器
     */
    private HandlerMethodReturnValueHandlerComposite returnValueHandlers = new HandlerMethodReturnValueHandlerComposite();
    /**
     * 数据绑定工厂
     */
    @Nullable
    private WebDataBinderFactory dataBinderFactory;

    public ServletInvocableHandlerMethod(Object bean, Method method) {
        super(bean, method);
    }

    public ServletInvocableHandlerMethod(HandlerMethod handlerMethod) {
        super(handlerMethod);
    }

    public void setHandlerMethodArgumentResolvers(HandlerMethodArgumentResolverComposite resolvers) {
        this.resolvers = resolvers;
    }

    public void setHandlerMethodReturnValueHandlers(HandlerMethodReturnValueHandlerComposite returnValueHandlers) {
        this.returnValueHandlers=returnValueHandlers;
    }

    public void invokeAndHandle(ServletWebRequest webRequest, ModelAndViewContainer mavContainer,Object... providedArgs) throws Exception {
        Object returnValue = invokeForRequest(webRequest, mavContainer, providedArgs);
        setResponseStatus(webRequest);
    }


    private Object invokeForRequest(NativeWebRequest request, ModelAndViewContainer mavContainer, Object[] providedArgs) throws Exception {
        //获取参数
        Object[] args = getMethodArgumentValues(request, mavContainer, providedArgs);
        //执行方法
        return doInvoke(args);
    }

    private Object doInvoke(Object[] args) {
        // TODO: 2023/3/8 执行方法
        return null;
    }

    /**
     * 解析参数
     */
    protected Object[] getMethodArgumentValues(NativeWebRequest request, @Nullable ModelAndViewContainer mavContainer,
                                               Object... providedArgs) throws Exception {
        if (providedArgs==null ||providedArgs.length==0){
            return new Object[0];
        }
        // TODO: 2023/3/8 后续处理还没有做，很复杂的
        MethodParameter[] parameters = getMethodParameters();
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            MethodParameter parameter = parameters[i];
            if (!this.resolvers.supportsParameter(parameter)) {
                throw new IllegalStateException("No suitable resolver");
            }
            try {
                args[i] = this.resolvers.resolveArgument(parameter, mavContainer, request, this.dataBinderFactory);
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
                throw ex;
            }
        }
        return args;

    }
    private void setResponseStatus(ServletWebRequest webRequest) {

    }
}
