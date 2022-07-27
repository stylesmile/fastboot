package io.github.stylesmile.handle;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import io.github.stylesmile.ioc.BeanContainer;
import io.github.stylesmile.ioc.BeanKey;
import io.github.stylesmile.request.RequestMethod;
import io.github.stylesmile.tool.JsonGsonUtil;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 保存每个URL和Controller的映射
 *
 * @author Stylesmile
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

    private final Parameter[] parameters;

    private final RequestMethod requestMethod;

    public MappingHandler(String uri, Method method, Class<?> controller, String[] args, Parameter[] parameters, RequestMethod requestMethod) {
        this.uri = uri;
        this.method = method;
        this.controller = controller;
        this.args = args;
        this.parameters = parameters;
        this.requestMethod = requestMethod;
    }

    /**
     * 处理方法
     *
     * @param httpExchange httpExchange
     * @return 是否处理了
     * @throws IllegalAccessException    异常
     * @throws InstantiationException    异常
     * @throws InvocationTargetException 异常
     * @throws IOException               异常
     */
    public boolean handle(HttpExchange httpExchange) throws IllegalAccessException, InstantiationException,
            InvocationTargetException, IOException {
        //获取请求路径
        String url = httpExchange.getRequestURI().getPath();
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
        List<Object> parameters2 = new CopyOnWriteArrayList<>();
        for (int i = 0; i < parameters.length; i++) {

            String parameterType = parameters[i].getParameterizedType().getTypeName();
            if (parameterType.equals("com.sun.net.httpserver.HttpExchange")) {
                parameters2.add(httpExchange);
                continue;
            }
            Object o = parameterMap.get(parameters[i].getName());
            buildParameters(parameters2, parameterType, o);
        }
        //从缓存中取出Controller，启动时就已经创建Controller实例了
        BeanKey beanKey = new BeanKey(controller, requestMethod.name());
        Object ctl = BeanContainer.getInstancesHasName(beanKey);
        if (ctl == null) {
            ctl = BeanContainer.getSingleInstance(controller);
        }
        //调用对应的接口方法，并获取响应结果
        Object[] strArray = (Object[]) parameters2.toArray();
        Object response = method.invoke(ctl, strArray);
        String responseString;
        if (response instanceof String) {
            responseString = response.toString();
        } else if (response instanceof Integer) {
            responseString = response.toString();
        } else {
            responseString = JsonGsonUtil.BeanToJson(response);
        }
        OutputStream outputStream = httpExchange.getResponseBody();
        Headers headers = httpExchange.getResponseHeaders();
        headers.set("Content-Type", "application/json; charset=utf-8");
        headers.set("Access-Control-Allow-Origin", "*");
        headers.set("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        headers.set("Access-Control-Allow-Headers", "Origin,X-Requested-With,Content-Type,Accept");

        httpExchange.sendResponseHeaders(200, responseString.length());
        //将响应结果写到外面
        outputStream.write(responseString.getBytes(StandardCharsets.UTF_8));
        return true;
    }

    private void buildParameters(List<Object> parameters2, String parameterType, Object o) {
        if (o != null) {
            switch (parameterType) {
                case "java.lang.Boolean":
                    parameters2.add(Boolean.valueOf(o.toString()));
                    break;
                case "java.lang.boolean":
                    parameters2.add(Boolean.valueOf(o.toString()));
                    break;
                case "java.lang.Byte":
                    parameters2.add(Byte.valueOf(o.toString()));
                    break;
                case "java.lang.byte":
                    parameters2.add(Byte.valueOf(o.toString()));
                    break;
                case "java.lang.Short":
                    parameters2.add(Short.valueOf(o.toString()));
                    break;
                case "java.lang.short":
                    parameters2.add(Short.valueOf(o.toString()));
                    break;
                case "java.lang.Integer":
                    parameters2.add(Integer.valueOf(o.toString()));
                    break;
                case "java.lang.int":
                    parameters2.add(Integer.valueOf(o.toString()));
                    break;
                case "java.lang.Long":
                    parameters2.add(Long.valueOf(o.toString()));
                    break;
                case "java.lang.long":
                    parameters2.add(Long.valueOf(o.toString()));
                    break;
                case "java.lang.Float":
                    parameters2.add(Float.valueOf(o.toString()));
                    break;
                case "java.lang.float":
                    parameters2.add(Float.valueOf(o.toString()));
                    break;
                case "java.lang.Double":
                    parameters2.add(Double.valueOf(o.toString()));
                    break;
                case "java.lang.double":
                    parameters2.add(Double.valueOf(o.toString()));
                    break;
                case "java.lang.String":
                    parameters2.add(o.toString());
                    break;
                case "java.lang.char":
                    parameters2.add(o.toString());
                    break;
                default:
                    parameters2.add(o.toString());
                    break;
            }
        }
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