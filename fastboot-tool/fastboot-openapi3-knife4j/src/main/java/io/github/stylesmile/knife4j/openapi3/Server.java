package io.github.stylesmile.knife4j.openapi3;

import lombok.Data;

@Data
public class Server{
    private String url = "http://localhost:8080";
    private String description = "test";
}