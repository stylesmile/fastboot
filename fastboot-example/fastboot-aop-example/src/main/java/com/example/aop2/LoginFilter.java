package com.example.aop2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**自定义注解*/
@Retention(RetentionPolicy.RUNTIME)//定义我们自己写的注解何时有效
@Target(ElementType.METHOD)//定义我们写的注解可以描述的成员
public @interface LoginFilter {

    //在注解里加了个loginDefine，就是为了给用户提供自定义实现，如根据loginDefine值不同做不同的登录处理。
    int loginDefine() default 0;

}
