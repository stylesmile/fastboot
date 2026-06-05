package io.github.stylesmile.milvus;

import io.github.stylesmile.ioc.BeanContainer;
import io.github.stylesmile.plugin.Plugin;
import io.milvus.client.MilvusClient;
import io.milvus.client.MilvusServiceClient;

/**
 * Milvus 向量数据库插件。
 *
 * @author Stylesmile
 */
public class MilvusPlugin implements Plugin {

    @Override
    public void start() {

    }

    @Override
    public void init() {
        MilvusConfig config = MilvusConfig.load();
        MilvusClient milvusClient = new MilvusServiceClient(config.toConnectParam());
        MilvusOperations operations = new MilvusClientOperations(milvusClient);
        MilvusTemplate template = new MilvusTemplate(operations);
        BeanContainer.setInstance(MilvusConfig.class, config);
        BeanContainer.setInstance(MilvusClient.class, milvusClient);
        BeanContainer.setInstance(MilvusOperations.class, operations);
        BeanContainer.setInstance(MilvusTemplate.class, template);
    }

    @Override
    public void end() {

    }
}
