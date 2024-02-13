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
public class ApplicationTest {

    public static void main(String[] args) {
        App.start(ApplicationTest.class, args);
    }

    /**
     * swagger 获取分组信息
     */
    @RequestMapping("/1")
    public String resources1() {
        return "[{\"name\":\"????\",\"url\":\"/v2/api-docs?group=????\",\"swaggerVersion\":\"2.0\",\"location\":\"/v2/api-docs?group=????\"}]";
    }
    @RequestMapping("/swagger-resources")
    public String resources() {
        return "[{\"name\":\"????\",\"url\":\"/v2/api-docs?group=????\",\"swaggerVersion\":\"2.0\",\"location\":\"/v2/api-docs?group=????\"}]";
    }


    /**
     * swagger 获取分组接口数据
     */
    @RequestMapping("/swagger/v2")
    public String api(String group) throws IOException {
        return "";
    }
}
