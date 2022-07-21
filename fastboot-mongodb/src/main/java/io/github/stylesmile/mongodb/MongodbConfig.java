package io.github.stylesmile.mongodb;

import io.github.stylesmile.annotation.Service;
import io.github.stylesmile.ioc.Bean;
import io.github.stylesmile.tool.PropertyUtil;
import io.github.stylesmile.tool.StringUtil;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;

@Service
public class MongodbConfig {

    @Bean
    public MongoTemplate fastMongoTemplate() {
        return new MongoTemplate(fastMongoDatabaseFactory());
    }

    /**
     * 获取 mongo工厂
     *
     * @return 工厂
     * uri = mongodb://username:password@127.0.0.1:27016/dbname
     */
    public MongoDatabaseFactory fastMongoDatabaseFactory() {
        String uri = PropertyUtil.getProperty("mongodb.uri");
        if (StringUtil.isEmpty(uri)) {
            String host = PropertyUtil.getProperty("mongodb.host");
            String port = PropertyUtil.getProperty("mongodb.port");
            String db = PropertyUtil.getProperty("mongodb.db");
            String username = PropertyUtil.getProperty("mongodb.username");
            String password = PropertyUtil.getProperty("mongodb.password");
            uri = "mongodb://" + username + password + "@" + host + ":" + port + "/" + db;
        }
        return new SimpleMongoClientDatabaseFactory(uri);
    }

}
