package com.example.aop2;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MemberSignature;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * 登录切面处理
 */

@Aspect//此注解描述的类为切面类，其内部可以定义切入点表达式和通知方法
public class LoginFilterAspect {
    private static final String TAG = "LoginFilterAspect";

    //@annotation(包名.注解名)
    @Pointcut("@annotation(com.example.aop2.LoginFilter)")
    public void LoginFilter() {//承载切入点表达式的定义，方法不写任何内容
    }


    // @Around("@annotation(com.app.pest.anno.LoginFilter)")
    @Around("LoginFilter()")
    public void aroundLoginPoint(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("切点");
        //获取用户实现的ILogin类，如果没有初始化就抛出异常。
//        ILoginFilter iLoginFilter = LoginAssistant.getInstance().getILoginFilter();
//        if (iLoginFilter == null) {
//            throw new RuntimeException("LoginManger没有初始化");
//        }
        //先得到方法的签名methodSignature，然后得到@LoginFilter注解，如果注解为空，就不再往下走。
        Signature signature = joinPoint.getSignature();
        if (!(signature instanceof MemberSignature)) {
            throw new RuntimeException("该注解只能用于方法上");
        }
        MethodSignature methodSignature = (MethodSignature) signature;
        LoginFilter loginFilter = methodSignature.getMethod().getAnnotation(LoginFilter.class);
        if (loginFilter == null) {
            return;
        }
//        Context mContext = LoginAssistant.getInstance().getApplicationContext();
//        调用iLogin的isLogin()方法判断是否登录，这个isLogin是留给使用者自己实现的，如果登录，就会继续执行方法体调用方法直到完成，如果没有登录，执行下一个
//        if (iLoginFilter.isLogin(mContext)) {//已经登录
//            joinPoint.proceed();//执行目标方法
//        } else {
//            iLoginFilter.login(mContext, loginFilter.loginDefine());
//    }

    }
}
