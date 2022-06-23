package com.example.demo.frame;

import com.example.demo.util.ClassScanner;

import java.util.List;

/**
 * 框架入口类
 */
public class MiniApplication {

    public static void run(Class<?> cls, String[] args) {
        try {
            long startTime = System.currentTimeMillis();

            //创建Tomcat服务，启动服务
            TomcatServer tomcatServer = new TomcatServer(args);
            tomcatServer.startServer();
            //获取所有的Class
            List<Class<?>> classList = ClassScanner.scanClasses(cls.getPackage().getName());
            //创建Bean工厂,扫描Class，创建被注解标注的类
            BeanFactory.initBean(classList);
            //扫描所有的类，找到所有Controller，建立Controller中每个方法和Url的映射关系
            HandlerManager.resolveMappingHandler(classList);
            long endTime = System.currentTimeMillis();
            System.out.println("started in : " + (endTime - startTime) + "ms");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}