package io.github.stylesmile.knife4j.openapi;

import lombok.Data;

import java.util.List;
import java.util.Set;

/**
 * swagger 方法信息
 */
@Data
public class MethodInfo {
    /**
     * controller 描述
     */
    private Set<String> tags;
    /**
     * 描述
     */
    private String description;
    private String operationId;
    /**
     * 参数
     */
    private List<SwaggerParameter> parameters;
    /**
     * 返回值
     */
    private List<Object> responses;

}
