package io.github.stylesmile.annotation;

import io.github.stylesmile.request.RequestMethod;

import java.lang.annotation.*;

/**
 * 接口方法注解，需要指定Url
 *
 * @author Stylesmile
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface RequestMapping {
    /**
     * 请求路径
     */
    String value();

    /**
     * 类型 get post put delete
     *
     * @return RequestMethod
     */
    RequestMethod method() default RequestMethod.GET;
}