package com.kewen.spring.web.sample.controller;
/**
 * @descrpition 
 * @author kewen
 * @since 2023-02-07 9:29
 */
public class HelloController {

    private HelloService helloService;

    public void hello(){
        String hello = helloService.hello();
        System.out.println("HelloController -> "+hello);
    }

    public HelloService getHelloService() {
        return helloService;
    }

    public void setHelloService(HelloService helloService) {
        this.helloService = helloService;
    }
}
