package io.github.stylesmile.handle;

import io.github.stylesmile.file.MultipartFile;
import io.github.stylesmile.file.UploadedFile;
import io.github.stylesmile.ioc.BeanContainer;
import io.github.stylesmile.ioc.BeanKey;
import io.github.stylesmile.parse.ParseParameterJlHttpServer;
import io.github.stylesmile.request.RequestMethod;
import io.github.stylesmile.server.Headers;
import io.github.stylesmile.server.Request;
import io.github.stylesmile.server.Response;
import io.github.stylesmile.tool.HeaderUtil;
import io.github.stylesmile.tool.JsonGsonUtil;
import io.github.stylesmile.tool.MultipartUtil;
import io.github.stylesmile.web.HtmlView;
import io.github.stylesmile.web.ModelAndView;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
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
    /**
     * 请求方法
     */
    private final RequestMethod requestMethod;

    /**
     * @param uri           url
     * @param method        方法
     * @param controller    控制器
     * @param parameters    参数
     * @param requestMethod 请求方法
     */
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
    public boolean handle(Request request, Response response) throws Exception {
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

        // form-data 数据解析文件上传需要
        if (HeaderUtil.isMultipartFormData(request.getHeaders())) {
            // 解析form-data
            //MultipartUtil.parseFormData(request,parameterMap);
            MultipartUtil.buildParamsAndFiles(request, parameterMap);
        }
        if (HeaderUtil.isApplicationJsonData(request.getHeaders())) {
            // 解析form-data
            //MultipartUtil.parseFormData(request,parameterMap);
            MultipartUtil.parseApplicationJson(request, parameterMap);
        }
        List<Object> parameters2 = new CopyOnWriteArrayList<>();
        for (int i = 0; i < parameters.length; i++) {
            String parameterType = parameters[i].getParameterizedType().getTypeName();
            switch (parameterType) {
                case "io.github.stylesmile.server.Response":
                    parameters2.add(response);
                    break;
                case "io.github.stylesmile.server.Request":
                    parameters2.add(request);
                    break;
                case "io.github.stylesmile.file.MultipartFile":
                    parameters2.add(parameters[i].getName());
                    break;
                default:
                    Object o = parameterMap.get(parameters[i].getName());
                    buildParameters(parameters2, parameterType, o, request, parameterMap);
            }
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
        String resResult;
        if (responseResult instanceof String) {
            resResult = responseResult.toString();
        } else if (responseResult instanceof Integer) {
            resResult = responseResult.toString();
        } else if (responseResult instanceof ModelAndView) {
            return true;
        } else if (responseResult instanceof HtmlView) {
            Headers headers = response.getHeaders();
            headers.add("Content-Type", "text/html; charset=utf-8");
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
            headers.add("Access-Control-Allow-Headers", "Origin,X-Requested-With,Content-Type,Accept");
            //将响应结果写到外面
            response.send(200, (((HtmlView) responseResult).getHtmlview()));
            return true;
        } else {
            resResult = JsonGsonUtil.BeanToJson(responseResult);
        }
        Headers headers = response.getHeaders();
        headers.add("Content-Type", "application/json; charset=utf-8");
//        headers.add("Content-Type", "application/json");
        headers.add("Access-Control-Allow-Origin", "*");
        headers.add("Access-Control-Allow-Methods", "GET,POST,PUT,DELETE,OPTIONS");
        headers.add("Access-Control-Allow-Headers", "Origin,X-Requested-With,Content-Type,Accept");
        //将响应结果写到外面
        response.send(200, resResult);
        return true;
    }

    /**
     * 构建参数
     *
     * @param parameters2
     * @param parameterType
     * @param o
     * @param request
     * @param parameterMap
     */
    private void buildParameters(List<Object> parameters2, String parameterType, Object o, Request request, Map<String, Object> parameterMap) throws ClassNotFoundException {
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
                case "io.github.stylesmile.file.MultipartFile":
                    parameters2.add(((MultipartFile) o));
                    break;
                case "io.github.stylesmile.file.UploadedFile":
                    parameters2.add(((UploadedFile) o));
                    break;
                default:

                    parameters2.add(o.toString());
                    break;
            }
        } else {
            if (HeaderUtil.isApplicationJsonData(request.getHeaders())) {
                try {
                    Class clazz = Class.forName(parameterType);
                    String json = parameterMap.get("fastboot__application__json__").toString();
                    Object o1 = JsonGsonUtil.GsonToBean(json, clazz);
                    parameters2.add(o1);
                } catch (Exception e) {
                    System.err.println(e);
                    throw new RuntimeException("The parameter format is incorrect");
                }

            }
        }
    }


}
