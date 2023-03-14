package com.kewen.spring.web.sample.controller.req;
/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-09
 */
public class HelloReq {
    private String name;
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
