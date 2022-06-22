package com.example.demo;

import org.apache.catalina.LifecycleException;

public class Application {
    public static void main(String[] args) {
        TomcatServer tomcatServer = new TomcatServer(args);
        try {
            tomcatServer.startServer();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }
}