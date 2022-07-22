package io.github.stylesmile.mongodb;

import com.mongodb.client.MongoDatabase;
import io.github.stylesmile.ioc.BeanContainer;
import io.github.stylesmile.plugin.Plugin;

/**
 * 参考文献 https://blog.csdn.net/qq_42413011/article/details/118640420
 *
 * @author hxm
 */
public class MongodbPlugin implements Plugin {

    @Override
    public void start() {

    }

    @Override
    public void init() {
        MongoDatabase mongoDatabase = MongodbConfig.getMongoDatabase();
        BeanContainer.setInstance(MongoDatabase.class, mongoDatabase);
    }

    @Override
    public void end() {

    }
}
