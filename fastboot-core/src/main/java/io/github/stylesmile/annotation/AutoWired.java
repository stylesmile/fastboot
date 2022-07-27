package io.github.stylesmile.annotation;

import java.lang.annotation.*;

/**
 * 依赖注入注解
 * @author Stylesmile
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
//作用于类属性上
@Target(ElementType.FIELD)
public @interface AutoWired {
}