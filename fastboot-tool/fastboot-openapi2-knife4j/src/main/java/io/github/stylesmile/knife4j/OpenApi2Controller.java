package io.github.stylesmile.knife4j;

import java.io.IOException;

import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.knife4j.openapi.OpenApi2Utils;

/**
 * Swagger api Controller
 *
 * @author noear
 * @since 2.3
 */
public class OpenApi2Controller {
    /**
     * swagger 获取分组信息
     */
//    @Produces("application/json; charset=utf-8")
    @RequestMapping("swagger-resources")
    public String resources() throws IOException {
        return OpenApi2Utils.getApiGroupResourceJson("swagger/v2");
    }

    /**
     * swagger 获取分组接口数据
     */
//    @Produces("application/json; charset=utf-8")
    @RequestMapping("swagger/v2")
    public String api( String group) throws IOException {
        return OpenApi2Utils.getApiJson( group);
    }
}