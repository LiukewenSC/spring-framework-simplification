package com.kewen.spring.web.sample.controller;

import com.kewen.spring.beans.factory.annotation.Autowired;
import com.kewen.spring.context.annotation.Component;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-02-14
 */
@Component
public class ControllerAnno {
    @Autowired
    private HelloService helloService;

    public String hello(){
        helloService.hello();
        return "ControllerAnno";
    }
}
