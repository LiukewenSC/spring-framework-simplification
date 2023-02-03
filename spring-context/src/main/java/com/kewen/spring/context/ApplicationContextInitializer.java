package com.kewen.spring.context;

public interface ApplicationContextInitializer<C extends  ApplicationContext> {
    void initialize(C applicationContext);
}
