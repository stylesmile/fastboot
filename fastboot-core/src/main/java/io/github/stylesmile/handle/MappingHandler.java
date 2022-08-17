package io.github.stylesmile.handle;

import io.github.stylesmile.ioc.BeanContainer;
import io.github.stylesmile.ioc.BeanKey;
import io.github.stylesmile.parse.ParseParameterJlHttpServer;
import io.github.stylesmile.request.RequestMethod;
import io.github.stylesmile.server.HTTPServer;
import io.github.stylesmile.server.Headers;
import io.github.stylesmile.server.Request;
import io.github.stylesmile.server.Response;
import io.github.stylesmile.tool.JsonGsonUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final Parameter[] parameters;

    private final RequestMethod requestMethod;

    public MappingHandler(String uri, Method method, Class<?> controller, Parameter[] parameters, RequestMethod requestMethod) {
        this.uri = uri;
        this.method = method;
        this.controller = controller;
        this.parameters = parameters;
        this.requestMethod = requestMethod;
    }

    /**
     * 处理方法
     *
     * @param request  Request
     * @param response Response
     * @return 是否处理了
     * @throws IllegalAccessException    异常
     * @throws InstantiationException    异常
     * @throws InvocationTargetException 异常
     * @throws IOException               异常
     */
    public boolean handle(Request request, Response response) throws IllegalAccessException, InstantiationException,
            InvocationTargetException, IOException {
        //获取请求路径
        String url = request.getPath();
        //不是当前的Controller处理，直接返回
        if (!url.equals(uri)) {
            return false;
        }

        //是当前Controller要处理的，准备方法参数，从Request对象中获取，获取到的值给反射调用

        Map<String, Object> parameterMap = new HashMap<>();
        //解析get参数
        ParseParameterJlHttpServer.parseGetParameters(request, parameterMap);
        //解析post参数
        ParseParameterJlHttpServer.parsePostParameters(request, parameterMap);
        List<Object> parameters2 = new CopyOnWriteArrayList<>();
        for (int i = 0; i < parameters.length; i++) {

            String parameterType = parameters[i].getParameterizedType().getTypeName();
            if (parameterType.equals("io.github.stylesmile.server.Response")) {
                parameters2.add(response);
                continue;
            }
            if (parameterType.equals("io.github.stylesmile.server.Request")) {
                parameters2.add(request);
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
        method.setAccessible(true);
        Object responseResult = method.invoke(ctl, strArray);
        String responseString;
        if (responseResult instanceof String) {
            responseString = responseResult.toString();
        } else if (responseResult instanceof Integer) {
            responseString = responseResult.toString();
        } else {
            responseString = JsonGsonUtil.BeanToJson(responseResult);
        }
        // OutputStream outputStream = response.getResponseBody()
        OutputStream outputStream = response.getOutputStream();
        Headers headers = response.getHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Origin,X-Requested-With,Content-Type,Accept");

        // httpExchange.sendResponseHeaders(200, responseString.length())
        response.send(200, responseString);
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


}