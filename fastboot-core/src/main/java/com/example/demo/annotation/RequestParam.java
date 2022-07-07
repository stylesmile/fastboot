package com.example.demo.annotation;

import java.lang.annotation.*;

/**
 * 请求参数注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
//需要作用于方法参数上
@Target(ElementType.PARAMETER)
public @interface RequestParam {
    /**
     * 指定请求参数的key
     * @return String
     */
    String value();
}