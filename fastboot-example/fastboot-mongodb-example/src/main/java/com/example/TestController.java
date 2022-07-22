package com.example;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import com.mongodb.client.result.UpdateResult;
import io.github.stylesmile.annotation.AutoWired;
import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

/**
 * mongodb 操作
 * 官方文档地址
 * https://www.mongodb.com/docs/drivers/java/sync/current/quick-start/
 */
@Controller
public class TestController {

    @AutoWired
    MongoDatabase mongoDatabase;

    /**
     * https://www.mongodb.com/docs/drivers/java/sync/current/usage-examples/insertOne/
     */
    @RequestMapping("/save")
    public void save() {
        User user = new User();
        user.setName("1");
        user.setAge(18);
        MongoCollection<Document> collection = mongoDatabase.getCollection("user");
        try {
            collection.insertOne(new Document()
                    .append("_id", new ObjectId())
                    .append("name", "Ski Bloopers")
                    .append("age", 10));
        } catch (MongoException me) {
            System.err.println("Unable to insert due to an error: " + me);
        }
    }

    /**
     * https://www.mongodb.com/docs/drivers/java/sync/v4.6/usage-examples/updateOne/
     */
    @RequestMapping("/update")
    public void update() {
        MongoCollection<Document> collection = mongoDatabase.getCollection("user");
        //查询条件
        Document query = new Document().append("name", "zhangsan");
        //需要修改的值
        Bson updates = Updates.combine(
                Updates.set("age", 99),
//                Updates.addToSet("name", "Sports"),
                Updates.currentTimestamp("lastUpdated"));
        //是否
        UpdateOptions options = new UpdateOptions().upsert(true);
        try {
            UpdateResult result = collection.updateOne(query, updates, options);
            System.out.println("Modified document count: " + result.getModifiedCount());
            System.out.println("Upserted id: " + result.getUpsertedId()); // only contains a value when an upsert is performed
        } catch (MongoException me) {
            System.err.println("Unable to update due to an error: " + me);
        }
    }


    /**
     * https://www.mongodb.com/docs/drivers/java/sync/current/usage-examples/findOne/
     *
     * @return List
     */
    @RequestMapping("/list")
    public List<User> list() {
        //只查询 id,name,age三个字段
        Bson resultFields = fields(include("_id", "name", "age"));

        MongoCollection<User> collection = mongoDatabase.getCollection("user", User.class);
        List<User> userList = new ArrayList<>();
        MongoCursor<User> userMongoCursor = collection.find(Filters.lt("age", 1))
                .projection(resultFields)
                .sort(Sorts.descending("age")).iterator();
        while (userMongoCursor.hasNext()) {

            userList.add(userMongoCursor.next());
        }
        return userList;
    }

    @RequestMapping("/list2")
    public List<User> list2() {
        //只查询 id,name,age三个字段
        Bson resultFields = fields(include("_id", "name", "age"));
        BasicDBObject query = new BasicDBObject();
        Bson idParam = new Document("$gte", 1);
        query.put("age", idParam);

        MongoCollection<Document> simpleDataCollection = mongoDatabase.getCollection("user");
        MongoCursor<Document> documents = simpleDataCollection.find(query)
                .projection(resultFields)
                //限制100条
                .limit(100)
                //排序
//                .sort(Sorts.orderBy("age"))
                .iterator();
        List<User> userList = new ArrayList<>();
        documents.forEachRemaining(document -> {
            User data = new User();
            Object id = document.get("_id");
//            data.setId(document.get("_id").toString());
            data.setAge(document.getInteger("age"));
            userList.add(data);
        });
        return userList;
    }
}
