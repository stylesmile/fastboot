package io.github.stylesmile.handle;

import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.request.RequestMethod;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Handler管理类
 *
 * @author Stylesmile
 */
public class HandlerManager {
    /**
     * Controller类中所有类方法和uri映射关系
     */
    private static final Map<String, MappingHandler> mappingHandlerList = new ConcurrentHashMap<>(32);

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
            RequestMethod requestMethod = method.getDeclaredAnnotation(RequestMapping.class).method();
            //获取形参上的RequestParam注解，拿取注解上定义的值
            // ArrayList<String> paramNameList = new ArrayList<>()

            Parameter[] parameters = method.getParameters();
            for (int i = 0; i < parameters.length; i++) {
                Parameter parameterOld = parameters[i];
//                if (parameterOld.isAnnotationPresent(RequestParam.class)) {
//                    String paraName = parameterOld.getDeclaredAnnotation(RequestParam.class).value();
//                    if (StringUtil.isNotEmpty(paraName)) {
//                        Field[] fields = Parameter.class.getDeclaredFields();
//                        for (Field field : fields) {
//                            //获取属性的类型
//                            field.setAccessible(true);
//                            //反射将对象设置到属性上
//                            try {
//                                field.set(parameterOld, paraName);
//                            } catch (IllegalAccessException e) {
//                                throw new RuntimeException(e);
//                            }
//                        }
//                    }
//                }
            }
            //参数集合转换为数组
            // String[] params = paramNameList.toArray(new String[paramNameList.size()])
            //参数收集完毕，构建一个MappingHandler
            MappingHandler mappingHandler = new MappingHandler(uri, method, cls, parameters, requestMethod);
            //保存到列表里
            // mappingHandlerList.add(mappingHandler)
            mappingHandlerList.put(uri, mappingHandler);
        }
    }

    public static Map<String, MappingHandler> getMappingHandlerList() {
        return mappingHandlerList;
    }

    public static MappingHandler getMappingHandler(String uri) {
        MappingHandler mappingHandler = mappingHandlerList.get(uri);
        if (mappingHandler != null) {
            return mappingHandler;
        } else {
            if (uri.equals("/index.html")) {
                mappingHandler = mappingHandlerList.get("/");
                return mappingHandler;
            }
        }
        return null;
    }

    public static Map<String, MappingHandler> getAllMappingHandler() {
        return mappingHandlerList;
    }

}
