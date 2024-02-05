package com.example.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class MyAspect {

    @Pointcut("execution(* com.example.controller.*.*(..))")
    public void serviceMethods() {

    }

    @Before("serviceMethods()")
    public void beforeServiceMethod() {

        System.out.println("Before service method execution.");
    }
}
