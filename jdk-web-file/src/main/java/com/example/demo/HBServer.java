package com.example.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

public class HBServer {

    public static void main(String[] args) {
        HttpServer httpServer;
        try {
            long time1 = System.currentTimeMillis();
            httpServer = HttpServer.create(new InetSocketAddress(9090), 0);
            httpServer.createContext("/opt", new OpHandler());
            // 放到线程池里执行
            httpServer.setExecutor(Executors.newCachedThreadPool());
            httpServer.start();
            long time2 = System.currentTimeMillis();
            System.out.println("started in " + (time2 - time1) + "ms");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}