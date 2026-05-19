package io.github.stylesmile.knife4j;

import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.handle.HandlerManager;
import io.github.stylesmile.handle.MappingHandler;
import io.github.stylesmile.knife4j.openapi.MethodInfo;
import io.github.stylesmile.knife4j.openapi.SwaggerInfo;
import io.github.stylesmile.knife4j.openapi.SwaggerParameter;
import io.github.stylesmile.knife4j.openapi.Tags;
import io.github.stylesmile.knife4j.openapi3.Info;
import io.github.stylesmile.knife4j.openapi3.OpenApi3Info;
import io.github.stylesmile.knife4j.openapi3.OpenApi3Parameter;
import io.github.stylesmile.knife4j.openapi3.Openapi3MethodInfo;
import io.github.stylesmile.knife4j.openapi3.Server;
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
//    public static String getApiGroupResourceJson() throws IOException {
//        return getApiGroupResourceJson("/swagger/v2");
//    }

    /**
     * 获取接口分组资源
     */
//    public static String getApiGroupResourceJson(String resourceUri) throws IOException {
//        Map<String, MappingHandler> mappingHandlerList = HandlerManager.getAllMappingHandler();
//
//        List<ApiGroupResource> resourceList = new ArrayList<>();
//        for (String key : mappingHandlerList.keySet()) {
//            if (StringUtil.isNotEmpty(key)) {
////                String group = bw.name();
////                String groupName = ((DocDocket) bw.raw()).groupName();
////                String url = resourceUri + "?group=" + group;
//            }
//        }
//        return JsonGsonUtil.objectToJson(resourceList);
//    }

    /**
     * 获取接口
     */
    public static String getApiJson(String group) throws IOException {
        Map<String, MappingHandler> mappingHandlerList = HandlerManager.getAllMappingHandler();

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

    /**
     * 获取接口
     */
    public static OpenApi3Info getOpenapi3Json(String group) throws IOException {
        Map<String, MappingHandler> mappingHandlerList = HandlerManager.getAllMappingHandler();
        Map<String, Object> paths = new HashMap<>();
        
        for (String key : mappingHandlerList.keySet()) {
            if (key.startsWith("/v3/api-docs")) {
                continue;
            }
            
            MappingHandler mappingHandler = mappingHandlerList.get(key);
            Class controllerClass = mappingHandler.getController();
            String exclude = "io.github.stylesmile.knife4j.OpenApi2Controller";
            if (exclude.equals(controllerClass.getName())) {
                continue;
            }
            
            Method method = mappingHandler.getMethod();
            RequestMapping mapping = method.getAnnotation(RequestMapping.class);
            
            // 获取接口类型 get post delete put
            String httpMethod = mapping.method().name().toLowerCase();
            if (StringUtil.isEmpty(httpMethod)) {
                httpMethod = "get";
            }
            
            // 创建方法信息
            Openapi3MethodInfo methodInfo = new Openapi3MethodInfo();
            
            // 生成唯一的 operationId（使用 HTTP 方法 + 路径）
            String operationId = httpMethod + key.replace("/", "_").replaceAll("[^a-zA-Z0-9_]", "");
            if (operationId.startsWith("_")) {
                operationId = operationId.substring(1);
            }
            methodInfo.setOperationId(operationId);
            
            // 解析 controller 标签
            Set<String> tags = new HashSet<>();
            if (controllerClass.isAnnotationPresent(Tag.class)) {
                Tag apiAnno = (Tag) controllerClass.getAnnotation(Tag.class);
                String tagStr = apiAnno.name();
                if (StringUtil.isNotEmpty(tagStr)) {
                    tags.add(tagStr);
                }
            }
            if (tags.isEmpty()) {
                tags.add("default");
            }
            methodInfo.setTags(tags);
            
            // 解析接口描述
            if (method.isAnnotationPresent(Operation.class)) {
                Operation operationAnno = method.getAnnotation(Operation.class);
                methodInfo.setSummary(operationAnno.summary());
                methodInfo.setDescription(operationAnno.description());
            }
            
            // 解析参数
            ParameterWrap[] parameterWraps = mappingHandler.getParameters();
            List<OpenApi3Parameter> openApi3Parameters = new CopyOnWriteArrayList<>();
            
            if (parameterWraps != null && parameterWraps.length > 0) {
                for (int i = 0; i < parameterWraps.length; i++) {
                    ParameterWrap parameterWrap = parameterWraps[i];
                    OpenApi3Parameter openApi3Parameter = new OpenApi3Parameter();
                    openApi3Parameter.setName(parameterWrap.getName());
                    openApi3Parameter.setIn("query"); // 默认是 query 参数
                    
                    // 尝试从参数注解获取更多信息
                    // 注意：@Parameter 和 @RequestParam 是标注在参数上的
                    java.lang.reflect.Parameter param = parameterWrap.getParameter();
                    if (param.isAnnotationPresent(Parameter.class)) {
                        Parameter parameterAnno = param.getAnnotation(Parameter.class);
                        openApi3Parameter.setDescription(parameterAnno.description());
                        openApi3Parameter.setRequired(parameterAnno.required());
                    }
                    
                    // 设置参数类型 schema
                    OpenApi3Parameter.ParameterSchema schema = new OpenApi3Parameter.ParameterSchema();
                    String typeName = param.getParameterizedType().getTypeName();
                    // 根据 Java 类型映射到 OpenAPI 类型
                    if (typeName.contains("String")) {
                        schema.setType("string");
                    } else if (typeName.contains("Integer") || typeName.contains("int")) {
                        schema.setType("integer");
                    } else if (typeName.contains("Long") || typeName.contains("long")) {
                        schema.setType("integer");
                    } else if (typeName.contains("Boolean") || typeName.contains("boolean")) {
                        schema.setType("boolean");
                    } else if (typeName.contains("Double") || typeName.contains("double") || 
                               typeName.contains("Float") || typeName.contains("float")) {
                        schema.setType("number");
                    } else {
                        schema.setType("string"); // 默认
                    }
                    openApi3Parameter.setSchema(schema);
                    
                    openApi3Parameters.add(openApi3Parameter);
                }
            }
            methodInfo.setParameters(openApi3Parameters);
            
            // 添加 responses（OpenAPI3 规范要求是 Map）
            Map<String, Object> responses = new HashMap<>();
            Map<String, Object> successResponse = new HashMap<>();
            successResponse.put("description", "Successful operation");
            responses.put("200", successResponse);
            methodInfo.setResponses(responses);
            
            // 构建 path
            Map<String, Object> methodMap = new HashMap<>();
            methodMap.put(httpMethod, methodInfo);
            
            // 如果路径已存在，合并方法
            if (paths.containsKey(key)) {
                @SuppressWarnings("unchecked")
                Map<String, Object> existingMethods = (Map<String, Object>) paths.get(key);
                existingMethods.putAll(methodMap);
            } else {
                paths.put(key, methodMap);
            }
        }
        
        // 构建 OpenAPI3Info
        OpenApi3Info openApi3Info = new OpenApi3Info();
        openApi3Info.setPaths(paths);
        
        // 设置 info
        Info info = new Info();
        info.setTitle("FastBoot API");
        info.setVersion("1.0.0");
        info.setDescription("FastBoot OpenAPI 3.0 Documentation");
        openApi3Info.setInfo(info);
        
        // 设置 servers
        List<Server> servers = new ArrayList<>();
        Server server = new Server();
        server.setUrl("http://localhost:8081");
        server.setDescription("Local development server");
        servers.add(server);
        openApi3Info.setServers(servers);
        
        // 设置 tags（从所有接口中收集）
        Set<String> allTags = new HashSet<>();
        for (Object pathObj : paths.values()) {
            @SuppressWarnings("unchecked")
            Map<String, Object> methods = (Map<String, Object>) pathObj;
            for (Object methodObj : methods.values()) {
                if (methodObj instanceof Openapi3MethodInfo) {
                    Openapi3MethodInfo mi = (Openapi3MethodInfo) methodObj;
                    if (mi.getTags() != null) {
                        allTags.addAll(mi.getTags());
                    }
                }
            }
        }
        
        List<io.github.stylesmile.knife4j.openapi3.Tag> tagList = new ArrayList<>();
        for (String tagName : allTags) {
            io.github.stylesmile.knife4j.openapi3.Tag tag = new io.github.stylesmile.knife4j.openapi3.Tag();
            tag.setName(tagName);
            tag.setDescription(tagName + " related endpoints");
            tagList.add(tag);
        }
        openApi3Info.setTags(tagList);
        
        // 设置 components（必需，即使为空）
        HashMap<String, Object> components = new HashMap<>();
        openApi3Info.setComponents(components);
        
        return openApi3Info;
    }
}
