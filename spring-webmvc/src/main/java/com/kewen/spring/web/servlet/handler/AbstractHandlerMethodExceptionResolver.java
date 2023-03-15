package com.kewen.spring.web.servlet.handler;

import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.web.method.HandlerMethod;
import com.kewen.spring.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @descrpition 抽象的异常处理解析器，
 *          主要用于确定是HandlerMethod方法
 * @author kewen
 * @since 2023-03-15
 */
public abstract class AbstractHandlerMethodExceptionResolver extends AbstractHandlerExceptionResolver{
    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        return doResolveHandlerMethodException(request,response, ((HandlerMethod) handler),ex);
    }

    @Nullable
    protected abstract ModelAndView doResolveHandlerMethodException(
            HttpServletRequest request, HttpServletResponse response, @Nullable HandlerMethod handlerMethod, Exception ex);

}
