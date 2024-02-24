package io.github.stylesmile.knife4j.openapi;

import lombok.Data;

import java.util.Set;

/**
 * swagger 参数信息
 */
@Data
public class SwaggerParameter {
    /**
     * 参数名称 英文
     */
    private String name;
    private String in;
    private String description;
    /**
     * 参数类型 String
     */
    private Set<String> schema;
    /**
     * 是否必须
     */
    private boolean required = false;

}
