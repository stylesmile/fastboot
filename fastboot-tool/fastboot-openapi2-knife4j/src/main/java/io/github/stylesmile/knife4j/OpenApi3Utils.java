package io.github.stylesmile.knife4j;

import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.handle.HandlerManager;
import io.github.stylesmile.handle.MappingHandler;
import io.github.stylesmile.knife4j.domain.ApiGroupResource;
import io.github.stylesmile.knife4j.openapi.*;
import io.github.stylesmile.parameter.ParameterWrap;
import io.github.stylesmile.tool.JsonGsonUtil;
import io.github.stylesmile.tool.StringUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Open Api v2 工具类
 *
 * @author noear
 * @since 2.4
 */
public class OpenApi3Utils {
    /**
     * 获取接口分组资源
     */
    public static String getApiGroupResourceJson() throws IOException {
        return getApiGroupResourceJson("/swagger/v2");
    }

    /**
     * 获取接口分组资源
     */
    public static String getApiGroupResourceJson(String resourceUri) throws IOException {
        Map<String, MappingHandler> mappingHandlerList = HandlerManager.getAllMappingHandler();
        ;

        List<ApiGroupResource> resourceList = new ArrayList<>();
        for (String key : mappingHandlerList.keySet()) {
            if (StringUtil.isNotEmpty(key)) {
//                String group = bw.name();
//                String groupName = ((DocDocket) bw.raw()).groupName();
//                String url = resourceUri + "?group=" + group;
            }
        }
        return JsonGsonUtil.objectToJson(resourceList);
    }

    /**
     * 获取接口
     */
    public static String getApiJson(String group) throws IOException {
        Map<String, MappingHandler> mappingHandlerList = HandlerManager.getAllMappingHandler();

        List<ApiGroupResource> resourceList = new ArrayList<>();
        List<Map<String, Object>> paths = new ArrayList<>();
        for (String key : mappingHandlerList.keySet()) {
            MappingHandler mappingHandler = mappingHandlerList.get(key);
            Class controllerClass = mappingHandler.getController();
            String exclude = "io.github.stylesmile.knife4j.OpenApi2Controller";
            if (exclude.equals(controllerClass.getName())) {
                continue;
            }
            // 接口
            Map<String, Object> path = new ConcurrentHashMap<>();
            // 接口方法
            Map<String, Object> methodMap = new ConcurrentHashMap<>();
            Method method = mappingHandler.getMethod();
            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            // 获取接口类型 get post delete
            String httpMethod = mapping.method().name().toLowerCase();
            String httpMethod2 = null;
            if (StringUtil.isEmpty(httpMethod)) {
                httpMethod = "get";
                httpMethod2 = "post";
            }
            Set<String> tags = new HashSet<>();
            MethodInfo methodInfo = new MethodInfo();
            String tagStr = null;
            String description = null;
            // 解析 controller 描述
            if (controllerClass.isAnnotationPresent(Tag.class)) {
                Tag apiAnno = (Tag) controllerClass.getAnnotation(Tag.class);
                tagStr = apiAnno.name();
                tags.add(tagStr);
                methodInfo.setTags(tags);
            }
            //解析接口描述
            if (method.isAnnotationPresent(Operation.class)) {
                Operation operationAnno = method.getAnnotation(Operation.class);
                description = operationAnno.description();
                methodInfo.setDescription(description);
            }
            ParameterWrap[] parameterWraps = mappingHandler.getParameters();

            // 解析参数
            List<SwaggerParameter> swaggerParameters = new CopyOnWriteArrayList<>();
            if (method.isAnnotationPresent(Parameters.class)) {
                Parameters parameters = method.getAnnotation(Parameters.class);
                Map<String, Parameter> parameterWrapMap = new ConcurrentHashMap();

                for (int i = 0; i < parameters.value().length; i++) {
                    parameterWrapMap.put(parameters.value()[i].name(), parameters.value()[i]);
                }

                for (int i = 0; i < parameterWraps.length; i++) {
                    SwaggerParameter swaggerParameter = new SwaggerParameter();
                    if (i < parameters.value().length) {
                        Parameter parameter = parameters.value()[i];
                        swaggerParameter.setDescription(parameter.description());
                        swaggerParameter.setRequired(parameter.required());
                    }
                    swaggerParameter.setName(parameterWraps[i].getName());
                    Set<String> schema = new HashSet<>();
                    schema.add(parameterWraps[i].getParameter().getParameterizedType().getTypeName());
                    swaggerParameter.setSchema(schema);
                    swaggerParameters.add(swaggerParameter);
                }
            } else if (method.isAnnotationPresent(Parameter.class)) {
                SwaggerParameter swaggerParameter = new SwaggerParameter();
                Parameter parameter = method.getAnnotation(Parameter.class);
                swaggerParameter.setDescription(parameter.description());
                swaggerParameter.setName(parameter.name());
                swaggerParameter.setRequired(parameter.required());
                Set<String> schema = new HashSet<>();
                schema.add(parameterWraps[0].getParameter().getParameterizedType().getTypeName());
                swaggerParameter.setSchema(schema);
                swaggerParameters.add(swaggerParameter);
            }
            methodInfo.setParameters(swaggerParameters);
            methodMap.put(httpMethod, methodInfo);
            path.put(key, methodMap);
            paths.add(path);
        }
        SwaggerInfo swaggerInfo = new SwaggerInfo();
        swaggerInfo.setPaths(paths);
        swaggerInfo.setInfo(new Info());
        swaggerInfo.setHost("");
        swaggerInfo.setTags(new Tags());
        return JsonGsonUtil.objectToJson(swaggerInfo);
    }
}
