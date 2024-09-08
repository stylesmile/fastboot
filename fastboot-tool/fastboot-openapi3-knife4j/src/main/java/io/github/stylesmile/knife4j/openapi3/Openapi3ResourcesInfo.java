package io.github.stylesmile.knife4j.openapi3;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Auto-generated: 2024-02-13 23:22:22
 *
 * @author Stylesmile
 */
@Data
@NoArgsConstructor
public class Openapi3ResourcesInfo {
    private String configUrl;
    private String oauth2RedirectUrl;
    private String operationsSorter;
    private String tagsSorter;
    /**
     * 服务器url
     */
    private List<OpenApiUrl> urls;
    private String validatorUrl;
}