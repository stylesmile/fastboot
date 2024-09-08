package io.github.stylesmile.knife4j.openapi;

import com.google.gson.annotations.SerializedName;
import io.github.stylesmile.knife4j.openapi3.Info;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * swagger参数信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SwaggerInfo {
    private String swagger = "3.0";
    private Info info;
    private String host;
    private String basePath;
    private Tags tags;
    private List<Map<String, Object>> paths;
    @SerializedName("x-openapi")
    private Xopenapi xopenapi;


}
