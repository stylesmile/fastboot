//package io.github.stylesmile.knife4j;
//
//import io.github.stylesmile.annotation.RequestMapping;
//import io.github.stylesmile.handle.MappingHandler;
//import io.github.stylesmile.knife4j.domain.DocDocket;
//import io.github.stylesmile.knife4j.domain.model.ApiEnum;
//import io.github.stylesmile.request.RequestMethod;
//import io.github.stylesmile.tool.PropertyUtil;
//import io.github.stylesmile.tool.StringUtil;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiOperation;
//
//import java.lang.reflect.ParameterizedType;
//import java.lang.reflect.Type;
//import java.util.Collection;
//import java.util.Map;
//
///**
// * @author noear
// * @since 2.3
// */
//public class BuilderHelper {
//    public static boolean isModel(Class<?> clz) {
//        if (clz.isAnnotationPresent(ApiModel.class)) {
//            return true;
//        }
//
//        if (clz.getName().startsWith("java")) {
//            return false;
//        }
//
//        if (clz.isPrimitive() || clz.isEnum()) {
//            return false;
//        }
//
//        if (Map.class.isAssignableFrom(clz) || Collection.class.isAssignableFrom(clz)) {
//            return false;
//        }
//
//        return true;
//    }
//
//    public static String getModelName(Class<?> clazz, Type type) {
//        String modelName = clazz.getSimpleName();
//
//        if (type instanceof ParameterizedType) {
//            //支持泛型
//            Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
//
//            if (typeArguments != null && typeArguments.length > 0) {
//                StringBuilder buf = new StringBuilder();
//                for (Type v : typeArguments) {
//                    if (v instanceof Class<?>) {
//                        buf.append(((Class<?>) v).getSimpleName()).append(",");
//                    }
//
//                    if (v instanceof ParameterizedType) {
//                        ParameterizedType v2 = (ParameterizedType) v;
//                        Type v22 = v2.getRawType();
//
//                        if (v22 instanceof Class<?>) {
//                            String name2 = getModelName((Class<?>) v22, v2);
//                            buf.append(name2).append(",");
//                        }
//                    }
//                }
//
//                if (buf.length() > 0) {
//                    buf.setLength(buf.length() - 1);
//
//                    modelName = modelName + "«" + buf + "»";
//                }
//            }
//        }
//
//        return modelName;
//    }
//
//    public static String getHttpMethod(MappingHandler actionHolder, ApiOperation apiAction) {
//        if (StringUtil.isBlank(apiAction.httpMethod())) {
//            RequestMethod methodType = actionHolder.getMethod().getAnnotation(RequestMapping.class).method();
//            if (methodType == null) {
//                return methodType.name();
//            } else {
//                return ApiEnum.METHOD_POST;
//            }
//        } else {
//            return apiAction.httpMethod();
//        }
//    }
//
//    /**
//     * 获取host配置
//     */
//    public static String getHost(DocDocket swaggerDock) {
//        String host = swaggerDock.host();
//        if (StringUtil.isBlank(host)) {
//            host = "localhost";
//            String port = PropertyUtil.getProperty("server.port");
//            if (StringUtil.isNotBlank(port)) {
//                host += ":" + port;
//            }
//            host += ":" + "8080";
//        }
//
//        return host;
//    }
//
//    /**
//     * 避免ControllerKey 设置前缀后,与swagger basePath 设置导致前端生成2次
//     */
//    public static String getControllerKey(Class<?> controllerClz) {
//        RequestMapping mapping = controllerClz.getAnnotation(RequestMapping.class);
//        if (mapping == null) {
//            return "";
//        }
//
//        String path = annoAlias(mapping.value(), mapping.value());
//        if (path.startsWith("/")) {
//            return path.substring(1);
//        } else {
//            return path;
//        }
//    }
//
//    public static String annoAlias(String v1, String v2) {
//        if (StringUtil.isEmpty(v1)) {
//            return v2;
//        } else {
//            return v1;
//        }
//    }
//}
