package com.example.knife4j;

import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.annotation.RequestParam;
import io.github.stylesmile.app.App;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * @author chenye
 */
@Tag(name = "test")
@Controller
public class FastbootKnife4jApplication {
    public static void main(String[] args) {
        App.start(FastbootKnife4jApplication.class, args);
    }

    @Parameter(name = "name", description = "姓名", required = true)
    @Operation(description = "hello方法描述",summary = "hello方法描述")
    @RequestMapping("/hello")
    public String hello(@RequestParam(value = "name") String name) {
        return "Hi:" + name;
    }

//    @RequestMapping("/v3/api-docs/swagger-config")
//    public String swagger_config(@RequestParam(value = "name") String name) {
//        return "{\n" +
//                "    \"configUrl\": \"/v3/api-docs/swagger-config\",\n" +
//                "    \"oauth2RedirectUrl\": \"http://localhost:8081/swagger-ui/oauth2-redirect.html\",\n" +
//                "    \"operationsSorter\": \"alpha\",\n" +
//                "    \"tagsSorter\": \"alpha\",\n" +
//                "    \"urls\": [\n" +
//                "        {\n" +
//                "            \"url\": \"/v3/api-docs/default\",\n" +
//                "            \"name\": \"default\"\n" +
//                "        }\n" +
//                "    ],\n" +
//                "    \"validatorUrl\": \"\"\n" +
//                "}";
//    }


}
