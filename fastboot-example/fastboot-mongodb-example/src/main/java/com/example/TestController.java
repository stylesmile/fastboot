package com.example;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.querydsl.mongodb.morphia.MorphiaQuery;
import com.sun.xml.internal.bind.v2.model.core.ID;
import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.ioc.Value;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static com.mongodb.client.model.Projections.fields;
import static com.mongodb.client.model.Projections.include;

@Controller
public class TestController {

    @Value(value = "mongodb.uri")
    private String uri;

    @RequestMapping("/")
    public String hello() {
        return "hello fastboot";
    }

    private MongoClient mongoClient = null;
    private Morphia morphia = new Morphia();
    private Datastore ds = null;

    @RequestMapping("/0")
    public List<User> hello0() {
        mongoClient = new MongoClient(new MongoClientURI(uri));
        morphia = new Morphia().map(User.class);
        ds = morphia.createDatastore(mongoClient, "tasks");
        QUser qUser = new QUser("user");

        User user = new User();
        user.setName("1");
        user.setAge(18);
        MorphiaQuery<User> query = new MorphiaQuery<User>(morphia, ds, qUser);
        List<User> list = query
                .where(qUser.name.eq("1"))
                .fetch();
        return list;
    }

    @RequestMapping("/2")
    public List<User> hello1() {
//        mongoClient = new MongoClient(uri);
        mongoClient = new MongoClient(new MongoClientURI(uri));
        MongoDatabase database = mongoClient.getDatabase("tasks");


        Bson resultFields = fields(include("_id","name","contents"));

        Long randomId =1L;

        Bson idParam = new Document("$gte",randomId);
        BasicDBObject query = new BasicDBObject();

        //query.put("markerId", idParam);
        MongoCollection<Document> simpleDataCollection = database.getCollection("user");
        MongoCursor<Document> documents = simpleDataCollection.find(query)
                .projection(resultFields).limit(100)
//        .sort(Sorts.orderBy(sort))
                .iterator();
        List<User> userList = new ArrayList<>();
        documents.forEachRemaining(document -> {
            User data = new User();
//            data.setId(document.getInteger("_id"));
            data.setName(document.getString("name"));
            userList.add(data);
        });
        return userList;
    }

}
