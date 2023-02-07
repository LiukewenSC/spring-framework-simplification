package com.kewen.spring.web.context.support;

import com.kewen.spring.beans.factory.config.BeanPostProcessor;
import com.kewen.spring.core.lang.Nullable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-02-07 13:42
 */
public class ServletContextAwareProcessor implements BeanPostProcessor {
    @Nullable
    private ServletContext servletContext;

    @Nullable
    private ServletConfig servletConfig;

    public ServletContextAwareProcessor(ServletContext servletContext, ServletConfig servletConfig) {
        this.servletContext = servletContext;
        this.servletConfig = servletConfig;
    }
}
