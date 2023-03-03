package com.kewen.spring.web.context;

import com.kewen.spring.context.ConfigurableApplicationContext;
import com.kewen.spring.core.lang.Nullable;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * @descrpition web的配置
 * @author kewen
 * @since 2023-02-07 11:25
 */
public interface ConfigurableWebApplicationContext extends ConfigurableApplicationContext,WebApplicationContext {

    String APPLICATION_CONTEXT_ID_PREFIX = WebApplicationContext.class.getName() + ":";

    void setServletContext(ServletContext servletContext);

    void setServletConfig(@Nullable ServletConfig servletConfig);


    @Nullable
    ServletConfig getServletConfig();


    void setConfigLocation(String configLocation);

    void setConfigLocations(String... configLocations);


    @Nullable
    String[] getConfigLocations();
}
