//package com.example.aop;
//
//import io.github.stylesmile.annotation.Service;
//import org.springframework.aop.framework.ProxyFactory;
//import org.springframework.aop.interceptor.SimpleTraceInterceptor;
//import org.springframework.aop.support.DefaultPointcutAdvisor;
//import org.springframework.aop.support.StaticMethodMatcherPointcut;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.context.ApplicationContext;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import com.example.myapp.MyService;
//import com.example.myapp.MyAspect;
//
//@Service
//public class MyApp {
//
//    public void run() {
//        ProxyFactory factory = new ProxyFactory(new MyServiceImpl()); // 创建目标对象的代理工厂
//        factory.addInterceptor(new SimpleTraceInterceptor()); // 添加一个简单的跟踪拦截器来输出方法调用的信息
//        factory.addAdvisor(new DefaultPointcutAdvisor(new MyAspect(), new StaticMethodMatcherPointcut() { // 添加自定义的切面和切入点匹配器
//            @Override
//            public boolean matches(Method method, Class<?> targetClass) {
//                return true; // 匹配所有方法，这里只是一个示例，需要根据实际需求进行修改。
//            }
//        }));
//        MyService proxy = (MyService) factory.getProxy(); // 创建代理对象并注入目标对象的方法调用逻辑和AOP逻辑。
//        proxy.doSomething(); // 调用代理对象的方法，将触发AOP逻辑的执行。
//    }
//}
