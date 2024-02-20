package com.example;

public class GlobalExceptionHandler implements Thread.UncaughtExceptionHandler {
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        System.out.println("线程 " + t.getName() + " 抛出了异常： " + e.getMessage());
        // 这里可以添加自定义的异常处理逻辑，例如记录日志、发送通知等
    }
}