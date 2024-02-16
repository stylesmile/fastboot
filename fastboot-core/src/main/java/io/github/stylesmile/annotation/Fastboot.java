package io.github.stylesmile.annotation;

import java.lang.annotation.*;

/**
 * @author Stylesmile
 */
@Documented
//作用于类上
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Fastboot {
    /**
     * 指定请求参数的key
     *
     * @return String
     */
    String scanPackage() default "";

    /**
     * 需要扫描的类
     */
    Class[] include() default {};

    /**
     * 需要排除的类
     */
    Class[] exclude() default {};
}
