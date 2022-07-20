package io.github.stylesmile.ioc;

import java.lang.annotation.*;

/**
 * Bean注解，标识一个类被框架容器管理
 */
@Documented
//作用于类上
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Bean {
}