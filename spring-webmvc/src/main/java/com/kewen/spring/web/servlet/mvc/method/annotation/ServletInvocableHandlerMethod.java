package com.kewen.spring.web.servlet.mvc.method.annotation;

import com.kewen.spring.core.MethodParameter;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.core.util.StringUtils;
import com.kewen.spring.http.HttpStatus;
import com.kewen.spring.web.bind.support.WebDataBinderFactory;
import com.kewen.spring.web.context.request.NativeWebRequest;
import com.kewen.spring.web.context.request.ServletWebRequest;
import com.kewen.spring.web.method.HandlerMethod;
import com.kewen.spring.web.method.support.HandlerMethodArgumentResolverComposite;
import com.kewen.spring.web.method.support.HandlerMethodReturnValueHandlerComposite;
import com.kewen.spring.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
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
    private HandlerMethodArgumentResolverComposite resolvers;

    /**
     * 返回值解析器
     */
    private HandlerMethodReturnValueHandlerComposite returnValueHandlers ;
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

    public void setDataBinderFactory(WebDataBinderFactory dataBinderFactory) {
        this.dataBinderFactory = dataBinderFactory;
    }

    public void invokeAndHandle(ServletWebRequest webRequest, ModelAndViewContainer mavContainer, Object... providedArgs) throws Exception {
        Object returnValue = invokeForRequest(webRequest, mavContainer, providedArgs);
        //如果有异常，则在request中设置异常状态码
        setResponseStatus(webRequest);

        //数据为空则判定是否需要返回了
        if (returnValue ==null){
            if (getResponseStatus() !=null && mavContainer.isRequestHandled()){
                return;
            }
        } else if (StringUtils.hasText(getResponseStatusReason())) {
            //异常也需要返回
            mavContainer.setRequestHandled(true);
            return;
        }
        //设置为为执行完请求
        mavContainer.setRequestHandled(false);
        this.returnValueHandlers.handleReturnValue(
                returnValue, getReturnValueType(returnValue), mavContainer, webRequest);

    }


    private Object invokeForRequest(NativeWebRequest request, ModelAndViewContainer mavContainer, Object[] providedArgs) throws Exception {
        //获取参数
        Object[] args = getMethodArgumentValues(request, mavContainer, providedArgs);
        //执行方法
        return doInvoke(args);
    }

    private Object doInvoke(Object[] args) {
        //没啥东西，就是处理了很多异常信息提示
        try {
            return getMethod().invoke(getBean(),args);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 解析参数
     */
    protected Object[] getMethodArgumentValues(NativeWebRequest request, @Nullable ModelAndViewContainer mavContainer,
                                               Object... providedArgs) throws Exception {

        MethodParameter[] parameters = getMethodParameters();
        if (parameters==null ||parameters.length==0){
            return new Object[0];
        }
        Object[] args = new Object[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            MethodParameter parameter = parameters[i];
            args[i] = findProvidedArgument(parameter, providedArgs);
            if (args[i] != null) {
                continue;
            }

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

    @Nullable
    protected static Object findProvidedArgument(MethodParameter parameter, @Nullable Object... providedArgs) {
        if (providedArgs != null) {
            for (Object providedArg : providedArgs) {
                if (parameter.getParameterType().isInstance(providedArg)) {
                    return providedArg;
                }
            }
        }
        return null;
    }

    /**
     * 如果有异常，则设置异常在返回中
     */
    private void setResponseStatus(ServletWebRequest webRequest) throws IOException {
        HttpStatus status = getResponseStatus();
        if (status == null) {
            return;
        }

        HttpServletResponse response = webRequest.getResponse();
        if (response != null) {
            String reason = getResponseStatusReason();
            if (StringUtils.hasText(reason)) {
                response.sendError(status.value(), reason);
            }
            else {
                response.setStatus(status.value());
            }
        }

        // To be picked up by RedirectView
        //webRequest.getRequest().setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, status);
    }
}
