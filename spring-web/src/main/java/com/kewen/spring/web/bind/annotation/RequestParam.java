package com.kewen.spring.web.bind.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @descrpition 
 * @author kewen
 * @since 2023-03-09
 */
@Target({ ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {
    String value() default "";
    boolean required() default true;

    String defaultValue() default ValueConstants.DEFAULT_NONE;
}
