package io.github.stylesmile.knife4j.openapi3;

import lombok.Data;

/**
 * openapi3 参数信息
 */
@Data
public class OpenApi3Parameter {
    /**
     * 参数名称 英文
     */
    private String name;
    private String in;
    private String description;
    /**
     * 是否必须
     */
    private boolean required = false;
    /**
     * 参数类型
     */
    private ParameterSchema schema;

    @Data
    public static class ParameterSchema {
        private String type;
    }

}
