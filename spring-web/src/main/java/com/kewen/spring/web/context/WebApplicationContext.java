package com.kewen.spring.web.context;

import com.kewen.spring.context.ApplicationContext;
import com.kewen.spring.core.lang.Nullable;

import javax.servlet.ServletContext;

public interface WebApplicationContext extends ApplicationContext {

    String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName() + ".ROOT";
    String SCOPE_REQUEST = "request";
    String SCOPE_SESSION = "session";
    String SCOPE_APPLICATION = "application";
    String SERVLET_CONTEXT_BEAN_NAME = "servletContext";
    String CONTEXT_PARAMETERS_BEAN_NAME = "contextParameters";
    String CONTEXT_ATTRIBUTES_BEAN_NAME = "contextAttributes";

    void setId(String id);

    @Nullable
    ServletContext getServletContext();

    void setServletContext(ServletContext servletContext);



    void refresh();

}
