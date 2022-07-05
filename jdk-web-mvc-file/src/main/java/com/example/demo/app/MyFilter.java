package com.example.demo.app;

import com.example.demo.handle.HandlerManager;
import com.example.demo.handle.MappingHandler;
import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URLDecoder;
import java.util.*;

public class MyFilter extends Filter {
    @Override
    public void doFilter(HttpExchange httpExchange, Chain chain) throws IOException {
        String url = httpExchange.getRequestURI().toString();
        System.out.println(url);
        //跳转controller
        goController(httpExchange);

//        Map<String, Object> parameters = new HashMap<>();
//        httpExchange.setAttribute("parameters", parameters);
//        parseGetParameters(httpExchange, parameters);
//        parsePostParameters(httpExchange, parameters);
        chain.doFilter(httpExchange);
//        System.out.println(parameters);
    }

    /**
     * 跳转controller
     *
     * @param httpExchange http
     * @throws IOException 异常
     */
    private void goController(HttpExchange httpExchange) throws IOException {
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

    @Override
    public String description() {
        return null;
    }


}
