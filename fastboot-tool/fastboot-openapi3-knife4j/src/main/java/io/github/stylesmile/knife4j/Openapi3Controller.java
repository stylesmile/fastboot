package io.github.stylesmile.knife4j;

import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.knife4j.openapi3.OpenApi3Info;
import io.github.stylesmile.knife4j.openapi3.OpenApiUrl;
import io.github.stylesmile.knife4j.openapi3.Openapi3ResourcesInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Swagger api Controller
 *
 * @author noear
 * @since 2.3
 */
@Controller
public class Openapi3Controller {



    @RequestMapping("/v3/api-docs/swagger-config")
    public Openapi3ResourcesInfo getOpenapi3ResourcesInfo() throws IOException {
        Openapi3ResourcesInfo resourcesInfo = new Openapi3ResourcesInfo();
        resourcesInfo.setConfigUrl("/v3/api-docs/swagger-config");
        resourcesInfo.setOauth2RedirectUrl("http://localhost:8081/swagger-ui/oauth2-redirect.html");
        resourcesInfo.setOperationsSorter("alpha");
        resourcesInfo.setTagsSorter("alpha");
        resourcesInfo.setValidatorUrl("");
        List<OpenApiUrl> urls = new ArrayList<>();
        urls.add(new OpenApiUrl("/v3/api-docs/default","default"));
        resourcesInfo.setUrls(urls);
        return resourcesInfo;
    }
    /**
     * swagger 获取分组接口数据
     */
    @RequestMapping("/v3/api-docs/default")
    public OpenApi3Info api2() throws IOException {
        return OpenApi3Utils.getOpenapi3Json("default");
    }
}
