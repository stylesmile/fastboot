package com.example.demo;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpsServer;

import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class App2 {
    private static HttpServer httpServer = null;
    private static ExecutorService executor = new ThreadPoolExecutor(10, 20,
            60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue(10));

    public static void main(String[] args) throws Throwable {
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

}
