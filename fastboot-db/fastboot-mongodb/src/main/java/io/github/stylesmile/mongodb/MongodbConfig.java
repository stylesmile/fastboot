package io.github.stylesmile.mongodb;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;
import io.github.stylesmile.tool.PropertyUtil;

public class MongodbConfig {

    static String db = PropertyUtil.getProperty("mongodb.db");
    static String host = PropertyUtil.getProperty("mongodb.host");
    static String port = PropertyUtil.getProperty("mongodb.port");
    static String username = PropertyUtil.getProperty("mongodb.username");
    static String password = PropertyUtil.getProperty("mongodb.password");

    /**
     * 获取 mongo工厂
     *
     * @return 工厂
     * uri = mongodb://username:password@127.0.0.1:27016/dbname
     */
    public static MongoDatabase getMongoDatabase() {
        String uri = "mongodb://" + username + ":" + password + "@" + host + ":" + port + "/" + db;
        MongoClient mongoClient = new MongoClient(new MongoClientURI(uri));
        MongoDatabase database = mongoClient.getDatabase(db);
        return database;
    }

}
