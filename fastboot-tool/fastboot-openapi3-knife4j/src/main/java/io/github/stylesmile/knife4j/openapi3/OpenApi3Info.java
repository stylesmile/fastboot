package io.github.stylesmile.knife4j.openapi3;

import com.google.gson.annotations.SerializedName;
import io.github.stylesmile.knife4j.openapi.Tags;
import io.github.stylesmile.knife4j.openapi.Xopenapi;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * swagger参数信息
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenApi3Info {
    private String openapi = "3.0.1";
    private Info info;
    private List<Server> servers;
    private List<io.github.stylesmile.knife4j.openapi3.Tag> tags;
    private Map<String, Object> paths;
    @SerializedName("x-openapi")
    private Xopenapi xopenapi;
    private HashMap<String, Object> components;


}
