package com.example.demo.handle;

import com.example.demo.annotation.Controller;
import com.example.demo.annotation.RequestMapping;
import com.example.demo.annotation.RequestParam;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Handler管理类
 */
public class HandlerManager {
    /**
     * Controller类中所有类方法和uri映射关系
     */
    private static final List<MappingHandler> mappingHandlerList = new ArrayList<>();

    /**
     * 找到所有Controller类
     *
     * @param classList 类的Class集合
     */
    public static void resolveMappingHandler(List<Class<?>> classList) {
        for (Class<?> cls : classList) {
            //判断是否使用了Controller注解
            if (cls.isAnnotationPresent(Controller.class)) {
                parseHandlerFromController(cls);
            }
        }
    }

    /**
     * 解析Controller上的注解
     */
    private static void parseHandlerFromController(Class<?> cls) {
        //获取类上的所有方法
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            //判断方法是否使用了RequestMapping注解，如果没有标识，不处理
            if (!method.isAnnotationPresent(RequestMapping.class)) {
                continue;
            }
            //获取RequestMapping注解上标识的uri值
            String uri = method.getDeclaredAnnotation(RequestMapping.class).value();
            //获取形参上的RequestParam注解，拿取注解上定义的值
            ArrayList<String> paramNameList = new ArrayList<>();
            List<Parameter> args2 = new ArrayList<>();
            Parameter[] parameters = method.getParameters();
//            for (Parameter parameter : parameters) {
//                HashMap<String, String> map = new HashMap<>(16);
//                if (parameter.isAnnotationPresent(RequestParam.class)) {
//                    String value = parameter.getDeclaredAnnotation(RequestParam.class).value();
//                    String parameterType = parameter.getParameterizedType().getTypeName();
//                    String value2value = parameter.getName();
//
//                    paramNameList.add(value);
//                    map.put(value, parameterType);
//                    args2.add(parameter);
//                } else {
////                    String value = parameter.getName();
////                    paramNameList.add(value);
//                }
//            }
            //参数集合转换为数组
            String[] params = paramNameList.toArray(new String[paramNameList.size()]);

            //参数收集完毕，构建一个MappingHandler
            MappingHandler mappingHandler = new MappingHandler(uri, method, cls, params, parameters);
            //保存到列表里
            mappingHandlerList.add(mappingHandler);
        }
    }

    public static List<MappingHandler> getMappingHandlerList() {
        return mappingHandlerList;
    }
}