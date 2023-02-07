package com.kewen.spring.context.support;

import com.kewen.spring.beans.factory.ConfigurableListableBeanFactory;
import com.kewen.spring.beans.factory.config.BeanFactoryPostProcessor;
import com.kewen.spring.beans.factory.config.BeanPostProcessor;
import com.kewen.spring.beans.factory.support.BeanDefinitionRegistry;
import com.kewen.spring.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @descrpition 注册代理工具
 * @author kewen
 * @since 2023-02-07 13:59
 */
public class PostProcessorRegistrationDelegate {
    /**
     * 处理beanDefinitionRegister的相关，包括 BeanDefinitionRegisterPostProcessor
     * @param beanFactory
     * @param beanFactoryPostProcessors
     */
    public static void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory, List<BeanFactoryPostProcessor> beanFactoryPostProcessors) {

        if (beanFactory instanceof BeanDefinitionRegistry){
            BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;

            List<BeanFactoryPostProcessor> definitionPostProcessors = new ArrayList<>();

            //处理 BeanDefinitionRegisterPostProcessor 函数
            List<BeanDefinitionRegistryPostProcessor> registryProcessors = new ArrayList<>();
            for (BeanFactoryPostProcessor postProcessor : beanFactoryPostProcessors) {
                if (postProcessor instanceof BeanDefinitionRegistryPostProcessor){
                    BeanDefinitionRegistryPostProcessor registryPostProcessor = (BeanDefinitionRegistryPostProcessor) postProcessor;
                    //BeanDefinitionRegistryPostProcessor第一次执行
                    registryPostProcessor.postProcessBeanDefinitionRegistry(registry);
                    registryProcessors.add(registryPostProcessor);
                } else {
                    definitionPostProcessors.add(postProcessor);
                }
            }

            //BeanDefinitionRegistryPostProcessor第二次执行  这里会再次执行beanFactory里面定义的 实现了PriorityOrdered的BeanDefinitionRegistryPostProcessor
            List<BeanDefinitionRegistryPostProcessor> currProcessors = new ArrayList<>();
            //... beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
            registryProcessors.addAll(currProcessors);
            //再次执行
            invokeBeanDefinitionRegistryPostProcessors(currProcessors,registry);


            //BeanDefinitionRegistryPostProcessor第二次执行  这里会再次执行beanFactory里面定义的 实现了PriorityOrdered的BeanDefinitionRegistryPostProcessor
            currProcessors.clear();
            //... beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
            registryProcessors.addAll(currProcessors);
            //再次执行
            invokeBeanDefinitionRegistryPostProcessors(currProcessors,registry);


            //BeanDefinitionRegistryPostProcessor第三次执行  这里会再次执行beanFactory里面定义的 实现了Ordered的BeanDefinitionRegistryPostProcessor
            currProcessors.clear();
            //... beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
            registryProcessors.addAll(currProcessors);
            //再次执行
            invokeBeanDefinitionRegistryPostProcessors(currProcessors,registry);



            //BeanDefinitionRegistryPostProcessor第四次执行  这里会再次执行beanFactory里面定义的 剩下的 BeanDefinitionRegistryPostProcessor
            currProcessors.clear();
            //... beanFactory.getBeanNamesForType(BeanDefinitionRegistryPostProcessor.class, true, false);
            registryProcessors.addAll(currProcessors);
            //再次执行
            invokeBeanDefinitionRegistryPostProcessors(currProcessors,registry);



            //再分别执行 BeanDefinitionRegistryPostProcessor 和 BeanFactoryPostProcessor
            invokeBeanFactoryPostProcessors(registryProcessors, beanFactory);
            invokeBeanFactoryPostProcessors(registryProcessors, beanFactory);


        } else {
            // 不是 BeanDefinitionRegistryPostProcessor 就直接执行 BeanFactoryPostProcessor
            invokeBeanFactoryPostProcessors(beanFactoryPostProcessors, beanFactory);
        }


        // 再次从 BeanFactory中拿出注册到容器的bean执行一次方法 ，
        //不太明白，明明前面也已经加载进去了，为啥还要从beanfactory中拿，
        // 如果是两个地方配置了分别执行，那为啥容器中拿出来的BeanFactoryPostProcessor却没有再执行BeanDefinitionRegisterPostprocessor
        //invokeBeanFactoryPostProcessors(null)


    }
    private static void invokeBeanDefinitionRegistryPostProcessors(
            Collection<? extends BeanDefinitionRegistryPostProcessor> postProcessors, BeanDefinitionRegistry registry) {

        for (BeanDefinitionRegistryPostProcessor postProcessor : postProcessors) {
            postProcessor.postProcessBeanDefinitionRegistry(registry);
        }
    }
    private static void invokeBeanFactoryPostProcessors(
            Collection<? extends BeanFactoryPostProcessor> postProcessors, ConfigurableListableBeanFactory beanFactory) {

        for (BeanFactoryPostProcessor postProcessor : postProcessors) {
            postProcessor.postProcessBeanFactory(beanFactory);
        }
    }


    /**
     * 注册 BeanPostProcessor
     * 按分组 priorityOrdered -> internal -> ordered -> nonOrdered 拿出容器中的bean加入beanFactory的 beanPostProcessor中
     * @param beanFactory
     * @param applicationContext
     */
    public static void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory, AbstractApplicationContext applicationContext) {
        String[] beanPostProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false);
        if(beanPostProcessorNames==null){
            return;
        }
        //此处同样根据排序与否 按分组 priorityOrdered -> internal -> ordered -> nonOrdered 拿出容器中的bean加入beanFactory的 beanPostProcessor中
        List<BeanPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
        List<BeanPostProcessor> internalPostProcessors = new ArrayList<>();
        List<String> orderedPostProcessorNames = new ArrayList<>();
        List<String> nonOrderedPostProcessorNames = new ArrayList<>();
        // 获取
        // 排序
        //按顺序执行
        // for each  priorityOrderedPostProcessors internalPostProcessors  orderedPostProcessorNames nonOrderedPostProcessorNames
        // beanFactory.addBeanPostProcessor(beanPostProcessor);

        //简化的理解
        for (String beanPostProcessorName : beanPostProcessorNames) {
            BeanPostProcessor beanPostProcessor = beanFactory.getBean(beanPostProcessorName, BeanPostProcessor.class);
            //这里简化了，没排序和处理
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }

        //这里又注入一次，岂不是两次了，而且注入的东西都一样
        beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(applicationContext));

    }














}
