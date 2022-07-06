package com.example.demo.app;

import com.example.demo.handle.HandlerManager;
import com.example.demo.handle.MappingHandler;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class MyHttpHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String url = httpExchange.getRequestURI().toString();
        System.out.println(url);
        //获取所有Controller和内部定义的接口方法列表
        List<MappingHandler> mappingHandlerList = HandlerManager.getMappingHandlerList();
        //找到当前请求Url对应的Controller接口处理方法
        for (MappingHandler mappingHandler : mappingHandlerList) {
            try {
                if (mappingHandler.handle(httpExchange)) {
                    return;
                }
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }
}
