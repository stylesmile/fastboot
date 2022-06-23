package com.example.demo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpServer;

public class HBServer {

    public static void main( String[] args ){
        HttpServer httpServer;
        try {
            httpServer = HttpServer.create(new InetSocketAddress(16621), 0);
            httpServer.createContext("/op", new OpHandler());
            // 放到线程池里执行
            httpServer.setExecutor(Executors.newCachedThreadPool());
            httpServer.start();
        } catch (IOException e) { e.printStackTrace(); }
    }
}