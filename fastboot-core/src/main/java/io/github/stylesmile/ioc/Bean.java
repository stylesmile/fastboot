package io.github.stylesmile.ioc;

import java.lang.annotation.*;

/**
 * Bean注解，标识一个类被框架容器管理
 *
 * @author Stylesmile
 */
@Documented
//作用于类上
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Bean {
    /**
     * bean 名称
     */
    String value();
}