package io.github.stylesmile.minio;

import io.github.stylesmile.tool.PropertyUtil;
import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Minio 配置类
 * 
 * @author Stylesmile
 */
public class MinioConfig {
    
    private static final Logger logger = LoggerFactory.getLogger(MinioConfig.class);
    
    /**
     * 获取 minio 客户端
     *
     * @return MinioClient，如果配置错误则返回 null
     */
    public static MinioClient getMinioClient() {
        try {
            String endpoint = PropertyUtil.getProperty("minio.endpoint");
            String accessKey = PropertyUtil.getProperty("minio.accessKey");
            String secretKey = PropertyUtil.getProperty("minio.secretKey");
            
            // 验证必需的配置
            if (endpoint == null || endpoint.isEmpty()) {
                logger.warn("Minio endpoint not configured");
                return null;
            }
            if (accessKey == null || accessKey.isEmpty()) {
                logger.warn("Minio access key not configured");
                return null;
            }
            if (secretKey == null || secretKey.isEmpty()) {
                logger.warn("Minio secret key not configured");
                return null;
            }
            
            logger.info("Connecting to Minio server: {}", endpoint);
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(endpoint)
                    .credentials(accessKey, secretKey)
                    .build();
            logger.info("Minio client initialized successfully");
            return minioClient;
        } catch (Exception e) {
            logger.error("Failed to initialize Minio client", e);
            return null;
        }
    }
}