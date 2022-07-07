package com.example.demo.annotation;

import java.lang.annotation.*;

/**
 * 接口方法注解，需要指定Url
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestMapping {
    /**
     * Url
     * @return String
     */
    String value();
}