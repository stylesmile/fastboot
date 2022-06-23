package com.example.demo;


import com.example.demo.app.App;

import java.io.IOException;

/**
 * 测试入口类
 */
public class Application {
    public static void main(String[] args) throws IOException {
        App.start(Application.class, args);
    }
}