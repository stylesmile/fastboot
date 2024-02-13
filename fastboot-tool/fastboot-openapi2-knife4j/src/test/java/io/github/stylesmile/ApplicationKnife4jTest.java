package io.github.stylesmile;


import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.app.App;

import java.io.IOException;

/**
 * 测试入口类
 *
 * @author stylesmile
 */
@Controller
public class ApplicationKnife4jTest {

    public static void main(String[] args) {
        App.start(ApplicationKnife4jTest.class, args);
    }

    /**
     * swagger 获取分组信息
     */
//    @RequestMapping("/swagger-resources")
//    public String resources() {
//        return "[{\"name\":\"????\",\"url\":\"/v2/api-docs?group=????\",\"swaggerVersion\":\"2.0\",\"location\":\"/v2/api-docs?group=????\"}]";
//    }
//
//
//    /**
//     * swagger 获取分组接口数据
//     */
//    @RequestMapping("/swagger/v2")
//    public String api(String group) throws IOException {
//        return "{\"swagger\":\"2.0\",\"info\":{\"description\":\"`????`,**????** # aaa\",\"version\":\"v4.0\",\"title\":\"Knife4j????\",\"termsOfService\":\"https://stackoverflow.com/\",\"contact\":{\"name\":\"????\",\"url\":\"https://docs.xiaominfo.com\",\"email\":\"xiaoymin@foxmail.com\"},\"license\":{\"name\":\"Apache 2.0\",\"url\":\"https://stackoverflow.com/\"}},\"host\":\"localhost:8081\",\"basePath\":\"/\",\"tags\":[{\"name\":\"首页模块\",\"x-order\":\"2147483647\"}],\"paths\":{\"/sayHi\":{\"get\":{\"tags\":[\"首页模块\"],\"summary\":\"向客人问好\",\"operationId\":\"sayHiUsingGET\",\"produces\":[\"*/*\"],\"parameters\":[{\"name\":\"name\",\"in\":\"query\",\"description\":\"姓名\",\"required\":true,\"type\":\"string\"}],\"responses\":{\"200\":{\"description\":\"OK\",\"schema\":{\"type\":\"string\"}},\"401\":{\"description\":\"Unauthorized\"},\"403\":{\"description\":\"Forbidden\"},\"404\":{\"description\":\"Not Found\"}},\"responsesObject\":{\"200\":{\"description\":\"OK\",\"schema\":{\"type\":\"string\"}},\"401\":{\"description\":\"Unauthorized\"},\"403\":{\"description\":\"Forbidden\"},\"404\":{\"description\":\"Not Found\"}},\"deprecated\":false,\"x-order\":\"2147483647\"}}},\"x-openapi\":{\"x-markdownFiles\":null,\"x-setting\":{\"language\":\"zh-CN\",\"enableSwaggerModels\":true,\"swaggerModelName\":\"Swagger Models\",\"enableReloadCacheParameter\":false,\"enableAfterScript\":true,\"enableDocumentManage\":true,\"enableVersion\":false,\"enableRequestCache\":true,\"enableFilterMultipartApis\":false,\"enableFilterMultipartApiMethodType\":\"POST\",\"enableHost\":false,\"enableHostText\":\"\",\"enableDynamicParameter\":false,\"enableDebug\":true,\"enableFooter\":true,\"enableFooterCustom\":false,\"footerCustomContent\":null,\"enableSearch\":true,\"enableOpenApi\":true,\"enableHomeCustom\":false,\"homeCustomLocation\":null,\"enableGroup\":true,\"enableResponseCode\":true}}}";
//    }

//    @RequestMapping("/swagger/v2")
//    public String api() throws IOException {
//        return "{\"swagger\":\"2.0\",\"info\":{\"description\":\"`????`,**????** # aaa\",\"version\":\"v4.0\",\"title\":\"Knife4j????\",\"termsOfService\":\"https://stackoverflow.com/\",\"contact\":{\"name\":\"????\",\"url\":\"https://docs.xiaominfo.com\",\"email\":\"xiaoymin@foxmail.com\"},\"license\":{\"name\":\"Apache 2.0\",\"url\":\"https://stackoverflow.com/\"}},\"host\":\"localhost:8081\",\"basePath\":\"/\",\"tags\":[{\"name\":\"首页模块\",\"x-order\":\"2147483647\"}],\"paths\":{\"/sayHi\":{\"get\":{\"tags\":[\"首页模块\"],\"summary\":\"向客人问好\",\"operationId\":\"sayHiUsingGET\",\"produces\":[\"*/*\"],\"parameters\":[{\"name\":\"name\",\"in\":\"query\",\"description\":\"姓名\",\"required\":true,\"type\":\"string\"}],\"responses\":{\"200\":{\"description\":\"OK\",\"schema\":{\"type\":\"string\"}},\"401\":{\"description\":\"Unauthorized\"},\"403\":{\"description\":\"Forbidden\"},\"404\":{\"description\":\"Not Found\"}},\"responsesObject\":{\"200\":{\"description\":\"OK\",\"schema\":{\"type\":\"string\"}},\"401\":{\"description\":\"Unauthorized\"},\"403\":{\"description\":\"Forbidden\"},\"404\":{\"description\":\"Not Found\"}},\"deprecated\":false,\"x-order\":\"2147483647\"}}},\"x-openapi\":{\"x-markdownFiles\":null,\"x-setting\":{\"language\":\"zh-CN\",\"enableSwaggerModels\":true,\"swaggerModelName\":\"Swagger Models\",\"enableReloadCacheParameter\":false,\"enableAfterScript\":true,\"enableDocumentManage\":true,\"enableVersion\":false,\"enableRequestCache\":true,\"enableFilterMultipartApis\":false,\"enableFilterMultipartApiMethodType\":\"POST\",\"enableHost\":false,\"enableHostText\":\"\",\"enableDynamicParameter\":false,\"enableDebug\":true,\"enableFooter\":true,\"enableFooterCustom\":false,\"footerCustomContent\":null,\"enableSearch\":true,\"enableOpenApi\":true,\"enableHomeCustom\":false,\"homeCustomLocation\":null,\"enableGroup\":true,\"enableResponseCode\":true}}}";
//    }
//
//    @RequestMapping("/v3/api-docs/swagger-config")
//    public String api3() throws IOException {
//        return "{\"configUrl\":\"/v3/api-docs/swagger-config\",\"oauth2RedirectUrl\":\"http://localhost:8082/swagger-ui/oauth2-redirect.html\",\"operationsSorter\":\"alpha\",\"tagsSorter\":\"alpha\",\"urls\":[{\"url\":\"/v3/api-docs/default\",\"name\":\"default\"}],\"validatorUrl\":\"\"}g";
//    }
}
