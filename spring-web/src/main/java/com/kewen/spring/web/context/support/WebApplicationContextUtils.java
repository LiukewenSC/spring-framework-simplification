package com.kewen.spring.web.context.support;

import com.kewen.spring.core.util.Assert;
import com.kewen.spring.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

/**
 * @author kewen
 * @descrpition
 * @since 2023-03-02
 */
public class WebApplicationContextUtils {
    private static final String ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE = WebApplicationContext.class.getName() + ".ROOT";

    public static WebApplicationContext getWebApplicationContext(ServletContext sc) {
        return getWebApplicationContext(sc, ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
    }
    public static WebApplicationContext getWebApplicationContext(ServletContext sc,String attrName) {
        Assert.notNull(sc, "ServletContext must not be null");
        Object attr = sc.getAttribute(attrName);
        if (attr == null) {
            return null;
        }
        if (attr instanceof RuntimeException) {
            throw (RuntimeException) attr;
        }
        if (attr instanceof Error) {
            throw (Error) attr;
        }
        if (attr instanceof Exception) {
            throw new IllegalStateException((Exception) attr);
        }
        if (!(attr instanceof WebApplicationContext)) {
            throw new IllegalStateException("Context attribute is not of type WebApplicationContext: " + attr);
        }
        return (WebApplicationContext) attr;
    }
}
