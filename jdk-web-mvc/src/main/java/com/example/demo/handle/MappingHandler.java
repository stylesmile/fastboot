package com.example.demo.handle;

import com.example.demo.request.ServletRequest;
import com.example.demo.request.ServletResponse;
import com.example.demo.tool.BeanFactory;
import com.example.demo.tool.Utils;
import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;

/**
 * 保存每个URL和Controller的映射
 */
public class MappingHandler {
    /**
     * 请求路径Uri
     */
    private final String uri;
    /**
     * Controller中对应的方法
     */
    private final Method method;
    /**
     * Controller类对象
     */
    private final Class<?> controller;
    /**
     * 调用方法时传递的参数
     */
    private final String[] args;

    public MappingHandler(String uri, Method method, Class<?> controller, String[] args) {
        this.uri = uri;
        this.method = method;
        this.controller = controller;
        this.args = args;
    }

    /**
     * 处理方法
     *
     * @param req 请求对象
     * @param res 响应对象
     * @return 是否处理了
     */
//    public boolean handle(ServletRequest req, ServletResponse res) throws IllegalAccessException, InstantiationException,
//            InvocationTargetException, IOException {
//        //获取请求路径
//        String requestUri = ((HttpServletRequest) req).getRequestURI();
//        //不是当前的Controller处理，直接返回
//        if (!requestUri.equals(uri)) {
//            return false;
//        }
//        //是当前Controller要处理的，准备方法参数，从Request对象中获取，获取到的值给反射调用
//        Object[] parameters = new Object[args.length];
//        for (int i = 0; i < args.length; i++) {
//            parameters[i] = req.getParameter(args[i]);
//        }
//        //从缓存中取出Controller，启动时就已经创建Controller实例了
//        Object ctl = BeanFactory.getBean(controller);
//        //调用对应的接口方法，并获取响应结果
//        Object response = method.invoke(ctl, parameters);
//        //将响应结果写到外面
//        res.getWriter().println(response.toString());
//        return true;
//    }
    public boolean handle(HttpExchange httpExchange) throws IllegalAccessException, InstantiationException,
            InvocationTargetException, IOException {
        //获取请求路径
        String requestUri = httpExchange.getRequestURI().toString();
        InputStream inputStream = httpExchange.getRequestBody();
        String requestBody = Utils.readLine(inputStream);
        System.out.println("requestBody: " + requestBody);
        byte[] bytes = inputStream.readAllBytes();
        for (Byte b : bytes) {
            System.out.print(b);
        }
        OutputStream outputStream = httpExchange.getResponseBody();
        //不是当前的Controller处理，直接返回
        if (!requestUri.equals(uri)) {
            return false;
        }

        //是当前Controller要处理的，准备方法参数，从Request对象中获取，获取到的值给反射调用
        Object[] parameters = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
//            parameters[i] = httpExchange.getRequestBody().getParameter(args[i]);
        }
        //从缓存中取出Controller，启动时就已经创建Controller实例了
        Object ctl = BeanFactory.getBean(controller);
        //调用对应的接口方法，并获取响应结果
        Object response = method.invoke(ctl, parameters);
        //将响应结果写到外面
        outputStream.write(response.toString().getBytes(StandardCharsets.UTF_8));
        return true;
    }
}