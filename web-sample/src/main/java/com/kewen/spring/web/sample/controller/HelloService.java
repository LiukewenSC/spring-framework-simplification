package com.kewen.spring.web.sample.controller;
/**
 * @descrpition 
 * @author kewen
 * @since 2023-02-07 9:31
 */
public class HelloService {

    private HelloController helloController;
    public String hello(){
        System.out.printf("hello");
        return "hello";
    }

    public HelloController getHelloController() {
        return helloController;
    }

    public void setHelloController(HelloController helloController) {
        this.helloController = helloController;
    }
}
