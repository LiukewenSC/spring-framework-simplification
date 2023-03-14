package com.kewen.spring.web.bind;

import com.kewen.spring.core.MethodParameter;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-09
 */
public class WebDataBinder {
    public Object convertIfNecessary(Object arg, Class<?> parameterType, MethodParameter parameter) {
        //处理参数转换，简单处理了，不弄那么麻烦，原框架此处逻辑很多
        if (arg instanceof String){
            if (parameterType==Integer.class){
                return Integer.valueOf((String) arg);
            } else if (parameterType == Long.class){
                return Long.valueOf(((String) arg));
            } else if (parameterType == String.class){
                return ((String) arg);
            }
        } else if (arg instanceof String[]){
            String[] args = (String[]) arg;
            if (parameterType==List.class){

            }
        }
        return arg;
    }
}
