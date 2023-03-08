package com.kewen.spring.web.servlet;

import com.kewen.spring.core.lang.Nullable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @descrpition 控制器链
 * @author kewen
 * @since 2023-03-08
 */
public class HandlerExecutionChain {

    /**
     * 一般为HandlerMethod
     */
    Object handler;

    @Nullable
    private List<HandlerInterceptor> interceptorList;

    private int interceptorIndex = -1;

    public HandlerExecutionChain(Object handler) {
        this.handler=handler;
        this.interceptorList = new ArrayList<>();
    }

    public List<HandlerInterceptor> getInterceptors(){
        return interceptorList;
    }

    public void addInterceptor(HandlerInterceptor interceptor) {
        interceptorList.add(interceptor);
    }

    public Object getHandler() {
        return this.handler;
    }

    public boolean applyPreHandle(HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<HandlerInterceptor> interceptors = getInterceptors();
        if (interceptors !=null){
            for (HandlerInterceptor interceptor : interceptors) {
                boolean isSuccess = interceptor.preHandle(request, response, getHandler());
                if (!isSuccess){
                    return false;
                }
                interceptorIndex++;
            }
        }
        return true;
    }

    public void applyPostHandle(HttpServletRequest request, HttpServletResponse response, ModelAndView mv) throws Exception {
        List<HandlerInterceptor> interceptors = getInterceptors();
        if (interceptors !=null){
            for (int i = interceptors.size() - 1; i>=0; i--) {
                interceptors.get(i).postHandle(request,response,this.handler,mv);
            }
        }
    }
    void triggerAfterCompletion(HttpServletRequest request, HttpServletResponse response, @Nullable Exception ex)
            throws Exception {

        List<HandlerInterceptor> interceptors = getInterceptors();
        if (interceptors != null) {
            for (int i = this.interceptorIndex; i >= 0; i--) {
                HandlerInterceptor interceptor = interceptors.get(i);
                try {
                    interceptor.afterCompletion(request, response, this.handler, ex);
                }
                catch (Throwable ex2) {
                    System.out.println(("HandlerInterceptor.afterCompletion threw exception" + ex2));
                }
            }
        }
    }
}
