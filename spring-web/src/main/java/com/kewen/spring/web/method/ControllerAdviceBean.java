package com.kewen.spring.web.method;

import cn.hutool.core.annotation.AnnotationUtil;
import com.kewen.spring.context.ApplicationContext;
import com.kewen.spring.core.lang.Nullable;
import com.kewen.spring.web.bind.annotation.ControllerAdvice;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-15
 */
public class ControllerAdviceBean {
    @Nullable
    private Object resolvedBean;

    public ControllerAdviceBean(@Nullable Object resolvedBean) {
        this.resolvedBean = resolvedBean;
    }

    public boolean isApplicableToBeanType(@Nullable Class<?> beanType) {
       return true;
    }


    public Object resolveBean() {
        return this.resolvedBean;
    }

    public Class<?> getBeanType(){
        return resolvedBean.getClass();
    }

    public static List<ControllerAdviceBean> findAnnotatedBeans(ApplicationContext context){
        // 简化处理带注解@ControllerAdvice的类
        Map<String, Object> objectMap = context.getBeansOfType(Object.class, true, false);
        ArrayList<ControllerAdviceBean> adviceBeans = new ArrayList<>();
        for (Object bean : objectMap.values()) {
            if (AnnotationUtil.hasAnnotation(bean.getClass(), ControllerAdvice.class)){
                adviceBeans.add(new ControllerAdviceBean(bean));
            }
        }
        return adviceBeans;
    }
}
