package io.github.stylesmile.minio;

import io.github.stylesmile.tool.PropertyUtil;
import io.minio.MinioClient;

public class MinioConfig {
    /**
     * 获取 minio 客户端
     * @return MinioClient
     */
    public static MinioClient getMinioClient() {
        String endpoint = PropertyUtil.getProperty("minio.endpoint");
        String accessKey = PropertyUtil.getProperty("minio.accessKey");
        String secretKey = PropertyUtil.getProperty("minio.secretKey");
        MinioClient minioClient = MinioClient.builder()
                .endpoint(endpoint)
                .credentials(accessKey, secretKey)
                .build();
        return minioClient;
    }
}