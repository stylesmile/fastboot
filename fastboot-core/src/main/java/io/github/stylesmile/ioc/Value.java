package io.github.stylesmile.ioc;

import java.lang.annotation.*;

/**
 * 类 名: Value
 * 描 述: 获取配置文件中的键值对
 * 邮 箱: huangjy19940202@gmail.com
 *
 * @author: Stylesmile
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Documented
public @interface Value {

    String value() default "";

}
