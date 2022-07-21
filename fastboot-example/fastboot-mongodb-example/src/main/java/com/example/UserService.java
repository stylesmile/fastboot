package com.example;

import io.github.stylesmile.annotation.Service;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {
    @Resource
    MongoTemplate mongoTemplate;

    public String save() {
        User user = new User();
        user.setAge(1);
        user.setName("1");
        return mongoTemplate.save(user).toString();
    }
    public List query() {
        Criteria criteria = new Criteria();
        criteria.and("name").is(1);
        List list = mongoTemplate.find(new Query(), User.class, "user");
        System.out.println(list);
        return list;
    }
}
