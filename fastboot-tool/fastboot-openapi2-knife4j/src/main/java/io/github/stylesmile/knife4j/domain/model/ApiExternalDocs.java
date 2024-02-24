package io.github.stylesmile.knife4j.domain.model;

/**
 * 接口扩展文档
 *
 * @author noear
 * @since 2.2
 */
public class ApiExternalDocs {
    private String description;
    private String url;

    public String description(){
        return description;
    }

    public String url(){
        return url;
    }

    public ApiExternalDocs() {

    }

    public ApiExternalDocs(String description, String url) {
        this.description = description;
        this.url = url;
    }
}
