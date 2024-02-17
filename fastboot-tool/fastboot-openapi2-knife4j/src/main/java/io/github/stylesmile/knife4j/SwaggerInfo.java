package io.github.stylesmile.knife4j;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;

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
    private HashMap<String,Object> paths;
    @SerializedName("x-openapi")
    private Xopenapi xopenapi;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private class Info {
        private String description = "swagger-ui";
        private String version = "2.7.4";
        private String title = "";
        private String termsOfService;

        private Contact contact;
        private License license;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        private class Contact {
            private String name;
            private String url;
            private String email;
        }

        @Data
        private class License {
            private String name = "test";
            private String url = "https://stackoverflow.com/";
        }
    }

    @Data
    private class Tags {
        private String name = "首页";
        private String x_order = "123456";
    }

    @Data
    private class Xopenapi {
        /**
         * 接口信息
         */
        private String name;
        private String x_order;
    }

}
