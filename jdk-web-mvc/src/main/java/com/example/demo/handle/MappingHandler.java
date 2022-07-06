package com.example.demo.handle;

import com.example.demo.tool.BeanFactory;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
        String url = httpExchange.getRequestURI().getPath();
//        InputStream inputStream = httpExchange.getRequestBody();
        //String requestBody = Utils.readLine(inputStream);
        //System.out.println("requestBody: " + requestBody);
//        byte[] bytes = inputStream.readAllBytes();
//        for (Byte b : bytes) {
//            System.out.print(b);
//        }
        OutputStream outputStream = httpExchange.getResponseBody();
        //不是当前的Controller处理，直接返回
        if (!url.equals(uri)) {
            return false;
        }

        //是当前Controller要处理的，准备方法参数，从Request对象中获取，获取到的值给反射调用

        Map<String, Object> parameterMap = new HashMap<>();
        //解析get参数
        parseGetParameters(httpExchange, parameterMap);
        //解析post参数
        parsePostParameters(httpExchange, parameterMap);
        httpExchange.setAttribute("parameters", parameterMap);
        Object[] parameters = new Object[args.length];
//        for (int i = 0; i < args.length; ) {
//            Object o = parameterMap.get(args[i]);
//            parameters[i] = o;
//            i++;
//        }
        parameters[0] = "1";
        //从缓存中取出Controller，启动时就已经创建Controller实例了
        Object ctl = BeanFactory.getBean(controller);
        //调用对应的接口方法，并获取响应结果
        Object response = method.invoke(ctl);
//        Object response = method.invoke(ctl, parameters);
        //将响应结果写到外面
        //将响应结果写到外面
        outputStream.write(response.toString().getBytes(StandardCharsets.UTF_8));
        return true;
    }

    /**
     * 解析get参数
     */

    private void parseGetParameters(HttpExchange exchange, Map<String, Object> parameters) throws UnsupportedEncodingException {
        URI requestedUri = exchange.getRequestURI();
        String query = requestedUri.getRawQuery();
        parseQuery(query, parameters);
    }

    /**
     * 解析post参数
     */
    private void parsePostParameters(HttpExchange exchange, Map<String, Object> parameters) throws IOException {
        String method = exchange.getRequestMethod();

        if ("POST".equalsIgnoreCase(method)
                || "PUT".equalsIgnoreCase(method)
                || "DELETE".equalsIgnoreCase(method)
                || "PATCH".equalsIgnoreCase(method)) {

            String ct = exchange.getRequestHeaders().getFirst("Content-Type");

            if (ct == null) {
                return;
            }

            if (ct.toLowerCase(Locale.US).startsWith("application/x-www-form-urlencoded") == false) {
                return;
            }

            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String query = br.readLine();
            parseQuery(query, parameters);
        }
    }

    private void parseQuery(String query, Map<String, Object> parameters) throws UnsupportedEncodingException {

        if (query != null) {
            String pairs[] = query.split("[&]");

            for (String pair : pairs) {
                String param[] = pair.split("[=]");

                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0], "UTF-8");
                }

                if (param.length > 1) {
                    value = URLDecoder.decode(param[1], "UTF-8");
                } else {
                    value = "";
                }

                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);

                    if (obj instanceof List<?>) {
                        List<String> values = (List<String>) obj;
                        values.add(value);
                    } else if (obj instanceof String) {
                        List<String> values = new ArrayList<String>();
                        values.add((String) obj);
                        values.add(value);
                        parameters.put(key, values);
                    }
                } else {
                    parameters.put(key, value);
                }
            }
        }
    }
}