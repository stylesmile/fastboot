package com.example.demo;

import javax.servlet.*;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 分发请求的Servlet
 */
public class DispatcherServlet implements Servlet {
    private ServletConfig config;

    @Override
    public void init(ServletConfig config) throws ServletException {
        this.config = config;
        System.out.println("DispatcherServlet => init()... 初始化");
    }

    @Override
    public ServletConfig getServletConfig() {
        return config;
    }

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        //获取所有Controller和内部定义的接口方法列表
        List<MappingHandler> mappingHandlerList = HandlerManager.getMappingHandlerList();
        //找到当前请求Url对应的Controller接口处理方法
        for (MappingHandler mappingHandler : mappingHandlerList) {
            try {
                if (mappingHandler.handle(req, res)) {
                    return;
                }
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String getServletInfo() {
        return "";
    }

    @Override
    public void destroy() {
        System.out.println("DispatcherServlet => destroy()... 销毁");
    }
}