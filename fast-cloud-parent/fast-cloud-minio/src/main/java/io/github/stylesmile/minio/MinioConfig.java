package io.github.stylesmile.minio;

import io.github.stylesmile.tool.PropertyUtil;
import io.minio.MinioClient;

public class MinioConfig {
    /**
     * 获取 minio 客户端
     *
     * @return MinioClient
     */
    public static MinioClient getMinioClient() {
        MinioClient minioClient = null;
        try {
            String endpoint = PropertyUtil.getProperty("minio.endpoint");
            String accessKey = PropertyUtil.getProperty("minio.accessKey");
            String secretKey = PropertyUtil.getProperty("minio.secretKey");
            minioClient = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();
        } catch (Exception e) {
            System.err.println("minio 配置错误" + e.getMessage());
        }
        return minioClient;
    }
}