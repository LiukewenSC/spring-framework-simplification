package com.kewen.spring.web.context;

import com.kewen.spring.context.ApplicationContextInitializer;

public class WebApplicationContextInitializer implements ApplicationContextInitializer<WebApplicationContext> {
    @Override
    public void initialize(WebApplicationContext applicationContext) {
        System.out.println("WebApplicationContextInitializer:initialize");
    }
}
