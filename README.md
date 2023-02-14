# spring-framework-simplification

## 基本说明

#### 介绍

spring框架学习目的使用

将spring-framework系列的框架理解拆分，变成简单的只保留主要流程的框架，只保留其核心内容

目的用于学习spring系列，总体设想为根据spring的思想梳理其主体，整理主要流程。

此框架因只保留了核心逻辑，因此存在大量bug和未知问题，也可能没有考虑到官方的强拓展性，胡乱在项目中使用后果自负哦


#### 软件架构
软件架构说明

和spring框架保持一致的命名、模块划分、功能分布，尽量的模仿原框架

#### 使用说明

1.  web 入口
    web的入口为 webapp/Web-INF/web.xml中，通过配置<listener>标签启动Spring容器
2.  xxxx
3.  xxxx

## 模块解析

### 依

### spring-core
    spring的核心模块，主要定义基础的工具、环境变量、流
### spring-beans
    主要与bean相关定义，包含了bean的创建工厂BeanFactory、bean的定义信息BeanDefinition、
    注册和创建bean、保存bean的信息
#### 整体结构 

![img.png](docs/image/img.png)

#### BeanFactory
    BeanFactory为bean的创建工厂抽象，主要定义了获取bean的方法
    很多接口和类都实现了beanfactory，因为beans包下主要就是对bean的操作
    其主要实现为 AbstractBeanFactory 抽象类，
#### ListableBeanFactory
    可配置的工厂，继承BeanFactory
    除了父定义，主要定义了 BeanDefinition的获取和校验方法
#### ConfigurableBeanFactory
    也是可配置的工厂，继承BeanFactory,SingletonBeanRegistry
    除了父定义，这里主要定义了扩展方法的添加，类别名等
#### ConfigurableListableBeanFactory
    继承ConfigurableBeanFactory，ListableBeanFactory
    配置接口将由大多数可列出的bean工厂实现。
    除了ConfigurableBeanFactory之外，它还提供了分析和修改bean定义以及预实例化单例的工具。
#### SingletonBeanRegistry / DefaultSingletonBeanRegistry
    单例容器注册接口，定义了单例bean的注册，获取方法
    实现类DefaultSingletonBeanRegistry内部包含了三级缓存的存储容器，实现了其定义的方法

#### AbstractBeanFactory
    实现ConfigurableBeanFactory,继承DefaultSingletonBeanRegistry
    因为继承了DefaultSingletonBeanRegistry，所以有能力实现Bean的获取，此抽象类完成了大部分的bean获取能力
    同时内部维护了beanPostProcessors列表，维护扩展能力
#### DefaultListableBeanFactory
    继承AbstractBeanFactory，同时实现了ConfigurableListableBeanFactory，BeanDefinitionRegistry
    最底层的实现类，维护了 beanDefinitionMap ，因此，得以实现创建bean的全过程，
    实现了ListableBeanFactory，因此也有获取bean定义的方法
    实现了BeanDefinitionRegistry，因此也有注册bean的方法


### spring-context


### 


## 其它

#### 参与贡献

1.  Fork 本仓库
2.  新建 Feat_xxx 分支
3.  提交代码
4.  新建 Pull Request


#### 特技

1.  使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2.  Gitee 官方博客 [blog.gitee.com](https://blog.gitee.com)
3.  你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解 Gitee 上的优秀开源项目
4.  [GVP](https://gitee.com/gvp) 全称是 Gitee 最有价值开源项目，是综合评定出的优秀开源项目
5.  Gitee 官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6.  Gitee 封面人物是一档用来展示 Gitee 会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)
