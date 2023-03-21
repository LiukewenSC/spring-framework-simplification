# spring-framework-simplification

## 基本说明

#### 介绍

spring框架学习目的使用

将spring-framework系列的框架理解拆分，变成简单的只保留主要流程的框架，只保留其核心内容

目的用于学习spring系列，总体设想为根据spring的思想梳理其主体，整理主要流程。

此框架因只保留了核心逻辑，因此存在大量bug和未知问题，也可能没有考虑到官方的强拓展性


#### 软件架构
软件架构说明

和spring框架保持一致的命名、模块划分、功能分布，尽量的模仿原框架

#### 使用说明

1.  web 入口
    web的入口为 webapp/Web-INF/web.xml中，通过配置<listener>标签启动Spring容器
2.  xxxx
3.  xxxx

## 模块解析

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
    上下文模块，主要定义了上下文相关的东西，如注解Component系列、扫描Component相关的流程及加载过程、上下文事件发布监听组件、上下文注入定义等
#### ComponentScanBeanDefinitionParser
    通过spring.handlers的spi获取扫描包对应的实现，可以实现对Component系列的扫描并加入容器
#### ApplicationContext
    定义了配置的位置和AutowireCapableBeanFactory工厂
#### ConfigurableApplicationContext
    定义了环境变量相关和获取bean工厂，刷新，定义了父上下文
#### AbstractApplicationContext
    定义了配置的位置、环境变量、监听器，同时刷新容器逻辑在此
    同时抽象了postProcessBeanFactory设置bean工厂、加载beanDefinition逻辑由子类实现
#### ApplicationContextAwareProcessor
    在initializeBean(注入bean)的时候执行调用，可以将上下文注入到实现了ApplicationContextAware的bean中，与BeanFactoryAware实现不一样
#### PostProcessorRegistrationDelegate 
    代理相关，目前还未深入看

### sprig-web
    定义了与servlet(header、session、request、response)相关的流程，异常处理的相关逻辑、web请求返回增强逻辑、web对应的上下文的定义及实现

#### WebApplicationContext
    定义了servletContext的获取
#### ConfigurableWebApplicationContext
    定义了配置地址和servletContext的配置
#### XmlWebApplicationContext
    实现了AbstractApplicationContext ，主要加载beandefinition到bean工厂中
#### HandlerMethodArgumentResolver
    参数解析器，定义了在request中取出的参数按照何种模式解析，通常使用三个实现处理
        RequestParamMethodArgumentResolver(处理@RequestParam)
        RequestResponseBodyMethodProcessor(处理@RequestBody)
        ServletModelAttributeMethodProcessor(处理默认不带注解的和ModelAttribute)
#### HandlerMethodReturnValueHandler
    返回处理解析器，本框架只实现了基于@ResponseBody的返回，因此只有RequestResponseBodyMethodProcessor



### sprig-webmvc
    大多为spring-web的逻辑上的方法具体实现。主要将controller与请求联系起来并做请求、返回的增强处理    
#### FrameworkServlet
    通过 initServletBean() 调起servlet初始化方法，然后执行创建及初始化上下文流程，预留给子类实现上下文完成后的刷新操作onRefresh。
    统一了请求调用方法doGet、doPost... 抽象doService出来给子类实现
#### DispatcherServlet
    实现FrameworkServlet的onRefresh方法，初始化9大组件
    实现了doService方法，并在自身doDispatch方法中执行请求处理
#### HandlerMapping
    只有一个getHandler()接口方法，通过请求拿到对应的拦截器链和执行方法，一般使用实现类RequestMappingHandlerMapping
    RequestMappingHandlerMapping 内部维护了controller方法的封装HandlerMethod对应的url请求连接，并在初始化组件阶段做好初始化的映射
#### HandlerAdapter
    包含supports()抽象方法和handle()抽象方法，supports()接收对应的支持的请求，handler()则执行对应的方法。
    其实是一种策略模式，并非适配器模式。
    通常实现类为RequestMappingHandlerAdapter，支持handler为HandlerMethod类型的，即与RequestMappingHandlerMapping对应
    RequestMappingHandlerAdapter：
        执行处理方法，构造ServletInvocableHandlerMethod，将初始化的参数解析器、返回值处理器加入其中并执行invocableMethod.invokeAndHandle()方法
        ServletInvocableHandlerMethod 包含解析参数、执行请求、返回值处理
#### HandlerInterceptor
    拦截器，执行HandlerAdapter之前和之后以及完成时处理的，正常时不能返回false或抛异常，否则直接跳过执行请求阶段进入异常处理返回阶段
    该拦截器链在获取HandlerMapping时构造，主要由使用者实现接口加入容器，初始化时会加载容器中的拦截器形成链
#### HandlerExceptionResolver
    异常处理解析器，主要处理异常，其主要实现为ExceptionHandlerExceptionResolver
    也会构造ServletInvocableHandlerMethod并执行方法，构造的执行方法为@ControllerAdvice对应的类和@ExceptionHandler方法
#### RequestResponseBodyMethodProcessor
    HandlerMethodArgumentResolver和HandlerMethodReturnValueHandler的实现
    主要处理@RequestBody和@ResponseBody对应的方法，分别在请求参数进入时做处理和对象返回时做处理
    其内部有ResponseBodyAdvice、RequestBodyAdvice的增强扩展，用于在执行读流或写流时做进一步增强
#### ResponseBodyAdvice、RequestBodyAdvice
    原框架为RequestResponseBodyAdvice，主要在RequestResponseBodyMethodProcessor中增强读流或写流的扩展能力

## 流程说明
### spring xml启动主体流程
由Tomcat的 ServletContextListener 类调起
在xml的servlet配置中 配置<context-param>参数，指定contextClass=ContextLoaderListener，此Class实现ServletContextListener，Tomcat启动时，会执行此类的contextInitialized方法，从而开始启动容器类
启动时先创建WebApplicationContext上下文对象，然后从ServletContext中加载父对象，一般而言，此时是没有的，然后配置和刷新。
配置主要是把servletContext的相关东西设置到上下文中，方便后续取用不再使用servlet。
然后配置制定的ApplicationContextInitializer应用上下文初始化器，并执行初始化（自定义配置上下文）。之后开始刷新操作。
上下文的刷新操作refresh()反应了启动上下文的全流程。

    1、首先创建beanFactory工厂，创建工厂的方法中会加载beanDefinition定义，beanDefinition保存了需要加载到容器中的实例的一些属性信息，为很重要的实体。
    2、然后添加系统的BeanPostProcessor到工厂中
    3、再执行BeanDefinitionRegisterPostProcessor的初始化，因为涉及到BeanDifinition，保证这部分的功能先执行;
    4、再执行BeanPostProcessor 将容器配置的BeanPostProcessor首先初始化完成，保证后续调用初始化bean的时候可以使用，也就是说这鞋bean优先初始化
    5、再执行一些 initMessageSource initApplicationEventMulticaster onRefresh 等
    6、再执行监听器的初始化，用于监听对应的事件
    7、再执行工厂完成的初始化事件，在这里，我们会初始化bean


### spring-webmvc
    主要加载webmvc的9大组件

#### 初始化流程
    1、spring初始化完成后 tomcat 继续调用javax.Servlet 的init方法执行servlet的初始化。
    2、然后在HttpServletBean -> FrameworkServlet中调用initWebApplicationContext()方法初始化上下文容器
    3、初始流程与spring类似，不同处在于此容器有parent容器即spring之前初始化过的容器，以后调用bean如果本容器没有也会先到parent中查找
    4、初始化完成后refresh方法会发布事件ContextRefreshedEvent，Framework会监听此事件并执行onRefresh()方法，本框架由于暂时未实现此观察模式，直接走的另外一个分支在完成容器初始化后调起刷新方法
    5、onRefresh()方法由子类DispatcherServlet实现，调用其initStrategies()方法初始化SpringMVC 9大组件
    6、initStrategies依次初始化-> 文件上传组件、本地语言组件、主题解析器组件、映射处理器组件、适配器处理器组件、异常处理器组件、请求转换组件、视图解析器组件、flash管理器组件

##### 文件上传组件
##### 本地语言组件
##### 主题解析器组件
##### 映射处理器组件
    先加载容器配置了的映射器HandlerMapping，若没有配置的则加载默认的3个并加入容器(配置在DispatcherServlet.properties中)
    主要用到RequestMappingHandlerMapping处理我们一般的@Controller方式的，BeanNameUrlHandlerMapping、RouterFunctionMapping很少用到

##### 适配器处理器组件
    先加载容器配置的HandlerAdapter ,若没有则加载默认的4个并加入容器(配置在DispatcherServlet.properties中)
    主要用到RequestMappingHandlerAdapter处理RequestMappingHandlerMapping对应的，其他3个很少用到

##### 异常处理器组件
    先加载容器配置的HandlerExceptionResolver ,若没有则加载默认的3个并加入容器(配置在DispatcherServlet.properties中)
    主要用到ExceptionHandlerExceptionResolver处理@ControllerAdvice对应的异常情况

##### 请求转换组件
##### 视图解析器组件
##### flash管理器组件

#### 请求调用流程
    javax.servlet.HttpServlet#service()启动请求调用
    FrameworkSerlvet#service()重写此方法正式进入框架调用，通过doService抽象方法提供给子类实现
    DispatcherServlet#doService记录请求后调用doDispatch()正式进入MVC的调用环节
    1、首先通过请求获取到映射器HandlerMapping，并组装成HandlerExecutionChain拦截器调用链
    2、然后根据拦截器链的处理方法(一般为HandlerMethod或其容器中beanName)获取到处理器适配器HandlerAdapter
    3、执行拦截器链的pre方法(拦截器链中的拦截器pre方法挨个执行)
    4、执行请求方法，调起controller
    5、执行拦截器的after方法(拦截器链中的拦截器post方法挨个执行)
    6、处理异常等
    7、finally中执行拦截器的complate方法
    
##### 获取请求映射器
    根据请求是否支持得到相应的映射器，
    一般使用的映射器为：
        RequestMappingHandlerMapping 将controller的方法封装成HandlerMethod，使得一个url对应一个HandlerMethod
##### 获取适配器
    根据映射器的获取到对应的处理器
    一般使用的处理器为：
        RequestMappingHandlerAdaptor 执行解析处理

##### 执行适配器方法
    1、HandlerAdapter#handler()为执行方法，通常为实现类RequestMappingHandlerMapping。 RequestMappingHandlerMapping主要执行方法为invokeHandlerMethod()，得到ModelAndView
    2、RequestMappingHandlerMapping#invokeHandlerMethod()构造一个ServletInvocableHandlerMethod执行器，并赋予初始值，此请求执行器将执行方法的全部过程
    3、ServletInvocableHandlerMethod#invokeAndHandle()分两步执行方法+处理好返回值
        ..invokeForRequest()中分为两步获取参数+执行方法
        ....getMethodArgumentValues()获取参数
        ....doInvoke()反射执行方法
        ..returnValueHandlers.handleReturnValue()中分为两步获取参数+执行方法

###### HandlerMethodArgumentResolver 参数解析
    在 ServletInvocableHandlerMethod.getMethodArgumentValues() 中获取并调用
    主要由参数解析器负责处理，所有参数中每一个参数均调用一次参数解析器集合resolvers并选取一个处理，得到参数数组  Object[] args
    一般使用到的参数解析器有：
        RequestParamMethodArgumentResolver 解析基本数据类型，加@RequestParam注解(或不加，不加涉及到asm解析参数暂未实现) 如 String类型，Integer类型等
        ServletModelAttributeMethodProcessor 解析对象类型(非基本类型)，从param中解析
        RequestResponseBodyMethodProcessor 解析@RequestBody对应的对象，从InputStream中获取流对象并解析，只能解析一次
            此对象中维护了 HttpMessageConverter 列表用于做参数解析的匹配器，当其中一个converter匹配解析时则处理数据，如jackson的处理器（这儿采用fastjson实现）
            RequestBodyAdvice请求增强也在这里实现前置后置增强
            

###### HandlerMethodReturnValueHandler 返回结果增强
    在ServletInvocableHandlerMethod#invokeAndHandle()方法执行完controller的处理后再使用returnValueHandler处理返回值之后的增强
    一般使用到的返回增强处理器有：
        RequestResponseBodyMethodProcessor 处理@ResponseBody对应的数据，将返回对象通过写入OutputStream中
            此对象中维护了 HttpMessageConverter 列表用于做写数据流的匹配器，当其中一个converter匹配时处理，如Jackson
            ResponseBodyAdvice返回增强也是在此处实现的

##### ModelAndView 处理
    暂时不处理此情况，只有在前后端不分离的情况下使用，暂且不做研究了

##### 异常处理
    springmvc中主要在DispatcherServlet中处理进入到doDispatch执行方法的异常情况，包括请求RequestMapping、Interceptor、RequestAdapter、业务异常等，通过try-catch异常在末尾统一通过processDispatchResult()方法处理异常
    1、DispatcherServlet中维护的handlerExceptionResolvers异常解析器来解析，一般用到ExceptionHandlerExceptionResolver方法，解析完成后则得到视图（对于前后端分离则直接将数据写入ResponseBody中）并返回
    2、ExceptionHandlerException，主要定义了返回处理器ReturnValueHandler(主要实现类RequestResponseBodyMethodProcessor)、转换器converter(主要实现类处理@ResponseBody的json处理器)。调用时组装ServletInvocableHandlerMethod调用，其逻辑同正常请求调用时处理方式
    3、ServletInvocableHandlerMethod，解析参数并执行方法最后执行后处理增强
        .. 解析参数是不必要的，调用时会传入目前的异常数据；
        .. 执行方法，执行的方法为定义的@ControllerAdvice类包含的异常处理类，在初始化时在ExceptionHandlerException定义好@ExceptionHandler的异常和方法的映射，解析异常时便匹配最接近的异常
        ....匹配最接近的异常是通过异常与其父类的层级关系深度来实现，找到最接近层级 如NullPointException异常在定义了RuntimeException 和Exception时会选择RuntimeException
    4、RequestResponseBodyMethodProcessor后处理增强会执行ResponseBodyAdvice的增强，最后主要通过JsonConverter将返回对象序列化并写入Response中
    5、RequestResponseBodyAdviceChain  后置增强处理流 在RequestResponseBodyMethodProcessor中，主要做在写流前的最后增强


## todo
    动态代理/切面逻辑
    bean的自动注入ByType注入暂未实现
    (03-27 ok)扩展函数 Aware、InitializingBean
    @PostConstruct、init-method
    (03-27 ok) ApplicationContextAware
    跨模块的classpath:META-INF/spring.handlers解析
    spring容器刷新过后springmvc又刷新一次，之后再看看咋弄
    springmvc的初始化应该从上下文监听器执行的，目前监听器功能还未实现，暂时未做
    @PathVariable解析未实现

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
