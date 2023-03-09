package com.kewen.spring.web.servlet;

import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.http.HttpStatus;

/**
 * @descrpition 返回模型
 * @author kewen
 * @since 2023-03-08
 */
public class ModelAndView {

    @Nullable
    private Object view;

    //@Nullable
    //private ModelMap model;

    @Nullable
    private HttpStatus status;

    private boolean cleared = false;
}
