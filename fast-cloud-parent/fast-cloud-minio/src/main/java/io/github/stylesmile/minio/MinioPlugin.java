package io.github.stylesmile.minio;

import io.github.stylesmile.ioc.BeanContainer;
import io.github.stylesmile.plugin.Plugin;
import io.minio.MinioClient;

/**
 * @author Stylesmile
 */
public class MinioPlugin implements Plugin {

    @Override
    public void start() {
        MinioClient minioClient = MinioConfig.getMinioClient();
        BeanContainer.setInstance(MinioClient.class, minioClient);
    }

    @Override
    public void init() {

    }

    @Override
    public void end() {

    }
}
