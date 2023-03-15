package com.kewen.spring.web.servlet.handler;

import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.web.servlet.HandlerExceptionResolver;
import com.kewen.spring.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author kewen
 * @descrpition 异常处理抽象类
 * @since 2023-03-15
 */
public abstract class AbstractHandlerExceptionResolver implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        response.addHeader("Cache-Control", "no-store");

        ModelAndView modelAndView = doResolveException(request, response, handler, ex);

        return modelAndView;
    }
    @Nullable
    protected abstract ModelAndView doResolveException(
            HttpServletRequest request, HttpServletResponse response, @Nullable Object handler, Exception ex);
}
