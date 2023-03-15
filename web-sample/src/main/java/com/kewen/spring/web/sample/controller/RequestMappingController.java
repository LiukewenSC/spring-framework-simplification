package com.kewen.spring.web.sample.controller;


import com.kewen.spring.web.bind.annotation.RequestBody;
import com.kewen.spring.web.bind.annotation.RequestMapping;
import com.kewen.spring.web.bind.annotation.RequestParam;
import com.kewen.spring.web.bind.annotation.ResponseBody;
import com.kewen.spring.web.bind.annotation.RestController;
import com.kewen.spring.web.sample.controller.req.HelloReq;

/**
 * @author kewen
 * @descrpition
 * @since 2023-03-06
 */
@RestController
@RequestMapping("/request/mapping")
public class RequestMappingController {

    @RequestMapping("/hello")
    @ResponseBody
    public String hello(){
        return "hello RequestMappingController";
    }
    @RequestMapping("/hello2")
    @ResponseBody
    public String hello2(@RequestParam("name") String name,@RequestParam("age") Integer age){
        return "success + "+name+ ":" +age;
    }
    @RequestMapping("/hello3")
    public HelloReq hello3(HelloReq req){
        return req;
    }
    @RequestMapping("/hello4")
    public HelloReq hello4(@RequestBody HelloReq req){
        return req;
    }
    @RequestMapping("/error")
    public HelloReq error(){
        throw new RuntimeException("error test");
    }

}
