package io.github.stylesmile.knife4j.openapi;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * swagger参数信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SwaggerInfo {
    private String swagger = "2.0";
    private Info info;
    private String host;
    private String basePath;
    private Tags tags;
    private List<Map<String, Object>> paths;
    @SerializedName("x-openapi")
    private Xopenapi xopenapi;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private class Info {
        private String description = "swagger-ui";
        private String version = "2.0.0";
        private String title = "";
        private String termsOfService;
        private Contact contact;
        private License license;
    }
}
