package com.kewen.spring.web.bind.annotation;

import com.kewen.spring.context.annotation.Component;
import com.kewen.spring.context.annotation.Controller;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-15
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Controller
public @interface ControllerAdvice {
    String value() default "";
}
