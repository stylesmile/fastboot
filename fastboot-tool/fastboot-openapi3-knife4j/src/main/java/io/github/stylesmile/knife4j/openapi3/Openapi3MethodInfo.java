package io.github.stylesmile.knife4j.openapi3;

import lombok.Data;

import java.util.List;
import java.util.Map;
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
     * 简短描述
     */
    private String summary;
    /**
     * 详细描述
     */
    private String description;
    private String operationId = "index";
    /**
     * 参数列表
     */
    private List<OpenApi3Parameter> parameters;
    /**
     * 返回值 - OpenAPI3 规范要求是 Map
     */
    private Map<String, Object> responses;

}
