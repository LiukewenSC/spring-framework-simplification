package com.kewen.spring.web.sample.service;

import com.kewen.spring.context.annotation.Service;

/**
 * @author kewen
 * @descrpition
 * @since 2023-03-02
 */
@Service("annoHelloService")
public class HelloServiceAnno {

    public String hello(){
        System.out.println("service hello");
        return "service hello";
    }

}
