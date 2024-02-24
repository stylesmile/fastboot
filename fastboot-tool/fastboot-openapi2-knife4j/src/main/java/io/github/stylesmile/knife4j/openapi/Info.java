package io.github.stylesmile.knife4j.openapi;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * swagger基本信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Info {
    private String description = "swagger-ui";
    private String version = "2.0.0";
    private String title = "";
    private String termsOfService;
    private Contact contact;
    private License license;
}