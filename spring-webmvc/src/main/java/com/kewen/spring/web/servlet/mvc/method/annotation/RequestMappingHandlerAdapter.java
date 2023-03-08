package com.kewen.spring.web.servlet.mvc.method.annotation;

import com.kewen.spring.web.method.HandlerMethod;
import com.kewen.spring.web.servlet.HandlerAdapter;
import com.kewen.spring.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author kewen
 * @descrpition
 * @since 2023-03-06
 */
public class RequestMappingHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return (handler instanceof HandlerMethod);
    }

    @Override
    public ModelAndView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return handleInternal(request,response,(HandlerMethod)handler);
    }
    protected ModelAndView handleInternal(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod){
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
    private ModelAndView invokeHandlerMethod(HttpServletRequest request, HttpServletResponse response, HandlerMethod handlerMethod) {

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
}
