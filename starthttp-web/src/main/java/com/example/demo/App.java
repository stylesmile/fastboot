package com.example.demo;

import org.smartboot.http.server.HttpBootstrap;
import org.smartboot.http.server.HttpServerConfiguration;

public class App {


    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();
        HttpBootstrap httpBootstrap = new HttpBootstrap();
        HttpServerConfiguration config = httpBootstrap.configuration();
        config.bannerEnabled(false);
        config.readBufferSize(1024 * 8);
        config.threadNum(Runtime.getRuntime().availableProcessors() + 2);
        config.readBufferSize(1000);
        config.setMaxFormContentSize(11);
        config.setHttpServerHandler(new MyHttpServerHandler());
        httpBootstrap.setPort(8080);
        httpBootstrap.start();
        long endTime = System.currentTimeMillis();
        System.out.println("smart http started in : " + (endTime - startTime) + "ms");
    }

}
