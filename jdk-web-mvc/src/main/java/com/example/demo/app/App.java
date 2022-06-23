package com.example.demo.app;

import com.example.demo.Application;
import com.example.demo.handle.HandlerManager;
import com.example.demo.tool.BeanFactory;
import com.example.demo.tool.ClassScanner;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class App {
    private static HttpServer httpServer = null;
    private static ExecutorService executor = new ThreadPoolExecutor(10, 20,
            60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue(10));

    public static void main(String[] args) throws Throwable {
        start();
    }

    private static void start() throws IOException {
        long startTime = System.currentTimeMillis();
        System.out.println("start server : Sun.net.HttpServer");
        httpServer = HttpsServer.create(new InetSocketAddress("localhost", 8080), 0);
        HttpContext httpContext = httpServer.createContext("/", new MyHttpHandler());
        httpContext.getFilters().add(new MyFilter());
        httpServer.setExecutor(executor);
        httpServer.start();
        long endTime = System.currentTimeMillis();
        System.out.println("started in : " + (endTime - startTime) + "ms");
    }

    public static void start(Class<Application> applicationClass, String[] args) {
        long startTime = System.currentTimeMillis();
        int port = 8080;
        System.out.println("start server  port :" + port);
        List<Class<?>> classList = null;
        try {
            httpServer = HttpsServer.create(new InetSocketAddress("localhost", port), 0);
            String package1 = applicationClass.getPackage().getName();
            //扫描所有的类，
            classList = ClassScanner.scanClasses(package1);
            //创建Bean工厂,扫描Class，创建被注解标注的类
            BeanFactory.initBean(classList);
            //找到所有Controller，建立Controller中每个方法和Url的映射关系
            HandlerManager.resolveMappingHandler(classList);
        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        //创建Bean工厂,扫描Class，创建被注解标注的类
        HttpContext httpContext = httpServer.createContext("/", new MyHttpHandler());
        httpContext.getFilters().add(new MyFilter());
        httpServer.setExecutor(executor);
        httpServer.start();
        long endTime = System.currentTimeMillis();
        System.out.println("started in : " + (endTime - startTime) + "ms");
    }
}
