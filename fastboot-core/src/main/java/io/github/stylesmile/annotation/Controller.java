package io.github.stylesmile.annotation;

import java.lang.annotation.*;

/**
 * 控制器注解
 */
@Documented
//保留到运行时
@Retention(RetentionPolicy.RUNTIME)
//作用到类上
@Target(ElementType.TYPE)
public @interface Controller {
}