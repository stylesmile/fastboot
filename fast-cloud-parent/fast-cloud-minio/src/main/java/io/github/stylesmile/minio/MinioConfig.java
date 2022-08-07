package io.github.stylesmile.minio;

import io.github.stylesmile.tool.PropertyUtil;
import io.minio.MinioClient;

public class MinioConfig {

    private static String endpoint = PropertyUtil.getProperty("minio.endpoint");
    private static String accessKey = PropertyUtil.getProperty("minio.accessKey");
    private static String secretKey = PropertyUtil.getProperty("minio.secretKey");

    public static MinioClient getMinioClient() {
        System.out.println(accessKey);
        System.out.println(secretKey);
        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        return minioClient;
    }
}