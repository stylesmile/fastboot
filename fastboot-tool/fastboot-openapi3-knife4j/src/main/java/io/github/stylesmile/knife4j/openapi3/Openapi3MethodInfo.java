package io.github.stylesmile.knife4j.openapi3;

import io.github.stylesmile.knife4j.openapi.SwaggerParameter;
import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * swagger 方法信息
 */
@Data
public class Openapi3MethodInfo {
    /**
     * controller 描述
     */
    private Set<String> tags;
    /**
     * 描述
     */
    private String summary;
    /**
     * 描述
     */
    private String description;
    private String operationId = "index";
    /**
     * 参数
     */
    private List<SwaggerParameter> parameters;
    /**
     * 返回值
     */
    private List<Object> responses;

}
