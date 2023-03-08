package com.kewen.spring.web.servlet.handler;

import com.kewen.spring.beans.exception.BeansException;
import com.kewen.spring.context.ApplicationContext;
import com.kewen.spring.context.ApplicationContextAware;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.web.servlet.HandlerExecutionChain;
import com.kewen.spring.web.servlet.HandlerInterceptor;
import com.kewen.spring.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @descrpition 抽象的映射器
 * @author kewen
 * @since 2023-03-08
 */
public abstract class AbstractHandlerMapping implements HandlerMapping  {
    @Nullable
    private Object defaultHandler;
    /**
     * todo 从标签中注入进来的，这里简化以下直接从容器中带进来，见RequestMappingHandlerMapping的setApplication接口
     *     <mvc:interceptors>
     *         <bean class="com.kewen.interceptor.GlobalInterceptor"/>
     *
     *         <mvc:interceptor>
     *             <mvc:mapping path="/**"/>
     *             <bean class="com.kewen.interceptor.HelloInterceptor"/>
     *         </mvc:interceptor>
     *     </mvc:interceptors>
     */
    protected final List<HandlerInterceptor> adaptedInterceptors = new ArrayList<>();


    @Override
    public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        //根据请求获取Handler
        Object handler = getHandlerInternal(request);
        if (handler==null){
            handler = defaultHandler;
        }
        if (handler==null){
            return null;
        }
        HandlerExecutionChain executionChain = getHandlerExecutionChain(handler, request);

        //如果有跨域则添加跨域拦截器，先不处理

        return executionChain;
    }

    /**
     * 获取Handler
     * @return  一般返回为HandlerMethod
     */
    protected abstract Object getHandlerInternal(HttpServletRequest request) throws Exception;

    protected HandlerExecutionChain getHandlerExecutionChain(Object handler, HttpServletRequest request) {
        //生成HandlerExecutionChain，一般为HandlerMethod
        HandlerExecutionChain chain = (handler instanceof HandlerExecutionChain) ?(HandlerExecutionChain)handler:new HandlerExecutionChain(handler);

        String lookupPath = request.getRequestURI();

        //加入interceptor
        for (HandlerInterceptor interceptor : this.adaptedInterceptors) {
            if (interceptor instanceof MappedInterceptor){
                MappedInterceptor mappedInterceptor = (MappedInterceptor) interceptor;
            /*if (mappedInterceptor.matches(lookupPath, this.pathMatcher)) {
                chain.addInterceptor(mappedInterceptor.getInterceptor());
            }*/
                chain.addInterceptor(mappedInterceptor.getInterceptor());

            } else {
                chain.addInterceptor(interceptor);
            }
        }

        return chain;

    }
}
