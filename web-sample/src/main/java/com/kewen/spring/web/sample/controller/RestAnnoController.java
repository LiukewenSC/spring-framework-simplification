package com.kewen.spring.web.sample.controller;

import com.kewen.spring.web.bind.annotation.RequestMapping;
import com.kewen.spring.web.bind.annotation.RestController;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-08
 */

@RequestMapping("/rest/anno")
@RestController
public class RestAnnoController {

    @RequestMapping("/hello")
    public String hello(){
        System.out.println("RestAnnoController hello");
        return "RestAnnoController hello";
    }

}
