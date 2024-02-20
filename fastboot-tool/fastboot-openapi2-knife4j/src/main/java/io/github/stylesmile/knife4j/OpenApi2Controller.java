package io.github.stylesmile.knife4j;

import java.io.IOException;

import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;

/**
 * Swagger api Controller
 *
 * @author noear
 * @since 2.3
 */
@Controller
public class OpenApi2Controller {
    /**
     * swagger 获取分组信息
     * {"configUrl":"/v3/api-docs/swagger-config","oauth2RedirectUrl":"http://localhost:8082/swagger-ui/oauth2-redirect.html","operationsSorter":"alpha","tagsSorter":"alpha","urls":[{"url":"/v3/api-docs/default","name":"default"}],"validatorUrl":""}
     */
    @RequestMapping("/swagger-resources")
    public String resources() {
        SwaggerResourcesInfo resourcesInfo = new SwaggerResourcesInfo();
        resourcesInfo.setName("fastboot");
        resourcesInfo.setUrl("/v2/api-docs?group=????");
        resourcesInfo.setSwaggerVersion("2.8.0");
        resourcesInfo.setLocation("/v2/api-docs?group=????");
//        return resourcesInfo;
        return "{\"configUrl\":\"/v3/api-docs/swagger-config\",\"oauth2RedirectUrl\":\"http://localhost:8082/swagger-ui/oauth2-redirect.html\",\"operationsSorter\":\"alpha\",\"tagsSorter\":\"alpha\",\"urls\":[{\"url\":\"/v3/api-docs/default\",\"name\":\"default\"}],\"validatorUrl\":\"\"}";
    }


    /**
     * swagger 获取分组接口数据
     */
    @RequestMapping("/swagger/v2")
    public String api() throws IOException {
        return "{\"swagger\":\"2.0\",\"info\":{\"description\":\"`????`,**????** # aaa\",\"version\":\"v4.0\",\"title\":\"Knife4j????\",\"termsOfService\":\"https://stackoverflow.com/\",\"contact\":{\"name\":\"????\",\"url\":\"https://docs.xiaominfo.com\",\"email\":\"xiaoymin@foxmail.com\"},\"license\":{\"name\":\"Apache 2.0\",\"url\":\"https://stackoverflow.com/\"}},\"host\":\"localhost:8081\",\"basePath\":\"/\",\"tags\":[{\"name\":\"首页模块\",\"x-order\":\"2147483647\"}],\"paths\":{\"/sayHi\":{\"get\":{\"tags\":[\"首页模块\"],\"summary\":\"向客人问好\",\"operationId\":\"sayHiUsingGET\",\"produces\":[\"*/*\"],\"parameters\":[{\"name\":\"name\",\"in\":\"query\",\"description\":\"姓名\",\"required\":true,\"type\":\"string\"}],\"responses\":{\"200\":{\"description\":\"OK\",\"schema\":{\"type\":\"string\"}},\"401\":{\"description\":\"Unauthorized\"},\"403\":{\"description\":\"Forbidden\"},\"404\":{\"description\":\"Not Found\"}},\"responsesObject\":{\"200\":{\"description\":\"OK\",\"schema\":{\"type\":\"string\"}},\"401\":{\"description\":\"Unauthorized\"},\"403\":{\"description\":\"Forbidden\"},\"404\":{\"description\":\"Not Found\"}},\"deprecated\":false,\"x-order\":\"2147483647\"}}},\"x-openapi\":{\"x-markdownFiles\":null,\"x-setting\":{\"language\":\"zh-CN\",\"enableSwaggerModels\":true,\"swaggerModelName\":\"Swagger Models\",\"enableReloadCacheParameter\":false,\"enableAfterScript\":true,\"enableDocumentManage\":true,\"enableVersion\":false,\"enableRequestCache\":true,\"enableFilterMultipartApis\":false,\"enableFilterMultipartApiMethodType\":\"POST\",\"enableHost\":false,\"enableHostText\":\"\",\"enableDynamicParameter\":false,\"enableDebug\":true,\"enableFooter\":true,\"enableFooterCustom\":false,\"footerCustomContent\":null,\"enableSearch\":true,\"enableOpenApi\":true,\"enableHomeCustom\":false,\"homeCustomLocation\":null,\"enableGroup\":true,\"enableResponseCode\":true}}}";
    }

    @RequestMapping("/v3/api-docs/swagger-config")
    public String api3() throws IOException {
        return "{\"configUrl\":\"/v3/api-docs/swagger-config\",\"oauth2RedirectUrl\":\"http://localhost:8082/swagger-ui/oauth2-redirect.html\",\"operationsSorter\":\"alpha\",\"tagsSorter\":\"alpha\",\"urls\":[{\"url\":\"/v3/api-docs/group1\",\"name\":\"group1\"}],\"validatorUrl\":\"\"}";
    }
    /**
     * swagger 获取分组接口数据
     */
    @RequestMapping("/v3/api-docs/group1")
    public String api2() throws IOException {
        return "{\"openapi\":\"3.0.1\",\"info\":{\"title\":\"OpenAPI definition\",\"version\":\"v0\"},\"servers\":[{\"url\":\"http://localhost:8082\",\"description\":\"Generated server url\"}],\"paths\":{},\"components\":{},\"x-openapi\":{\"x-setting\":{\"customCode\":200,\"language\":\"zh-CN\",\"enableSwaggerModels\":true,\"swaggerModelName\":\"Swagger Models\",\"enableReloadCacheParameter\":false,\"enableAfterScript\":true,\"enableDocumentManage\":true,\"enableVersion\":false,\"enableRequestCache\":true,\"enableFilterMultipartApis\":false,\"enableFilterMultipartApiMethodType\":\"POST\",\"enableHost\":false,\"enableHostText\":\"\",\"enableDynamicParameter\":false,\"enableDebug\":true,\"enableFooter\":true,\"enableFooterCustom\":false,\"enableSearch\":true,\"enableOpenApi\":true,\"enableHomeCustom\":false,\"enableGroup\":true,\"enableResponseCode\":true},\"x-markdownFiles\":[]}}";
    }
}
