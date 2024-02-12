package io.github.stylesmile.knife4j;

import java.io.IOException;

import io.github.stylesmile.annotation.RequestMapping;

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
    @RequestMapping("swagger-resources")
    public String resources() throws IOException {
        return "";
    }

    /**
     * swagger 获取分组接口数据
     */
    @RequestMapping("swagger/v2")
    public String api( String group) throws IOException {
        return "";
    }
}