package com.kewen.spring.web.servlet;

import cn.hutool.core.collection.CollectionUtil;
import com.kewen.spring.beans.BeanWrapper;
import com.kewen.spring.beans.BeanWrapperImpl;
import com.kewen.spring.beans.MutablePropertyValues;
import com.kewen.spring.beans.PropertyValue;
import com.kewen.spring.beans.PropertyValues;
import com.kewen.spring.core.ConfigurableEnvironment;
import com.kewen.spring.core.util.StringUtils;
import com.kewen.spring.web.context.support.StandardServletEnvironment;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * @author kewen
 * @descrpition
 * @since 2023-03-02
 */
public abstract class HttpServletBean extends HttpServlet {

    private final Set<String> requiredProperties = new HashSet<>(4);
    ConfigurableEnvironment  environment = new StandardServletEnvironment();


    @Override
    public void init() throws ServletException {

        PropertyValues pvs = new ServletConfigPropertyValues(getServletConfig(), this.requiredProperties);

        if (!pvs.isEmpty()){

            BeanWrapper bw = new BeanWrapperImpl(this);
            //扩展点
            initBeanWrapper(bw);
            // bw.setPropertyValues(pvs, true);
        }
        initServletBean();

    }

    protected abstract void initServletBean();

    /**
     * 扩展点
     * @param bw
     */
    protected void initBeanWrapper(BeanWrapper bw) {

    }

    public ConfigurableEnvironment getEnvironment() {
        return this.environment;
    }

    /**
     * key value 映射
     */
    private static class ServletConfigPropertyValues extends MutablePropertyValues {

        public ServletConfigPropertyValues(ServletConfig config, Set<String> requiredProperties)
                throws ServletException {

            Set<String> missingProps = (!CollectionUtil.isEmpty(requiredProperties) ?
                    new HashSet<>(requiredProperties) : null);

            Enumeration<String> paramNames = config.getInitParameterNames();
            while (paramNames.hasMoreElements()) {
                String property = paramNames.nextElement();
                Object value = config.getInitParameter(property);
                addOrReplacePropertyValue(new PropertyValue(property, value));
                if (missingProps != null) {
                    missingProps.remove(property);
                }
            }

            // Fail if we are still missing properties.
            if (!CollectionUtil.isEmpty(missingProps)) {
                throw new ServletException(
                        "Initialization from ServletConfig for servlet '" + config.getServletName() +
                                "' failed; the following required properties were missing: " + missingProps);
            }
        }
    }






}
