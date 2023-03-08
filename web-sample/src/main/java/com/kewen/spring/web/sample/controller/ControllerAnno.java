package com.kewen.spring.web.sample.controller;

import com.kewen.spring.beans.factory.annotation.Autowired;
import com.kewen.spring.context.annotation.Component;
import com.kewen.spring.context.annotation.Controller;
import com.kewen.spring.web.bind.annotation.RequestMapping;
import com.kewen.spring.web.bind.annotation.ResponseBody;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-02-14
 */
@Controller
public class ControllerAnno {
    @Autowired
    private HelloService helloService;

    @RequestMapping("/conAnno/hello")
    @ResponseBody
    public String hello(){
        helloService.hello();
        return "ControllerAnno";
    }
}
