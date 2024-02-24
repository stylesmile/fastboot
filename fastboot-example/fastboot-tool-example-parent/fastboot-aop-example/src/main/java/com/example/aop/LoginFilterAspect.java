package com.example.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MemberSignature;
import org.aspectj.lang.reflect.MethodSignature;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

/**
 * 登录切面处理
 */

@Aspect//此注解描述的类为切面类，其内部可以定义切入点表达式和通知方法
public class LoginFilterAspect {

    //@annotation(包名.注解名)
    @Pointcut("@annotation(com.example.aop.LoginFilter)")
    public void LoginFilter() {//承载切入点表达式的定义，方法不写任何内容
    }


    // @Around("@annotation(com.example.app.LoginFilter)")
    @Around("LoginFilter()")
    public Object aroundLoginPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        Object proceed = joinPoint.proceed();
        System.out.println("切点");
        long start = System.nanoTime();
        long end = System.nanoTime();
        System.out.println(
                String.format("[%s] method [%s] execution time: %s ",
                Thread.currentThread().getName(),
                joinPoint.getSignature().getName(),
                (end - start)));
        return proceed;
    }
}
