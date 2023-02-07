package com.kewen.spring.beans.factory;
/**
 * @descrpition 智能初始化bean，在bean完成之后执行
 * @author kewen
 * @since 2023-02-07 16:58
 */
public interface SmartInitializingSingleton {
    void afterSingletonsInstantiated();
}
