package io.github.stylesmile.knife4j.openapi3;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OpenApiUrl {
    /**
     * 服务器url
     */
    private String url;
    /**
     * 分组名称
     */
    private String name;
}
