package io.github.stylesmile.knife4j.openapi3;

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
    private String title = "OpenAPI definition";
    private String version = "v0";
}