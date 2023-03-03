package com.kewen.spring.web.servlet;

import com.kewen.spring.context.ApplicationContext;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @descrpition springmvc的入口
 * @author kewen
 * @since 2023-03-02
 */
public class DispatcherServlet extends FrameworkServlet {

    public DispatcherServlet() {
        System.out.println("创建DispatcherServlet");
    }

    /**
     * //初始化9大组件
     * @param wac
     */
    @Override
    protected void onRefresh(ApplicationContext wac) {

    }
}
