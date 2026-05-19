package io.github.stylesmile.minio;

import io.github.stylesmile.ioc.BeanContainer;
import io.github.stylesmile.plugin.Plugin;
import io.minio.MinioClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Minio 对象存储插件
 * 
 * @author Stylesmile
 */
public class MinioPlugin implements Plugin {
    
    private static final Logger logger = LoggerFactory.getLogger(MinioPlugin.class);

    @Override
    public void start() {
        MinioClient minioClient = MinioConfig.getMinioClient();
        
        if (minioClient == null) {
            logger.warn("Minio client initialization failed, skipping Minio plugin");
            return;
        }
        
        BeanContainer.setInstance(MinioClient.class, minioClient);
        logger.info("Minio plugin started successfully");
    }

    @Override
    public void init() {

    }

    @Override
    public void end() {

    }
}
