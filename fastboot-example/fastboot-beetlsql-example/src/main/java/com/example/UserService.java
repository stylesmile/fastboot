package com.example;

import io.github.stylesmile.annotation.AutoWired;
import io.github.stylesmile.annotation.Service;
import org.beetl.sql.core.SQLManager;

import java.util.List;

@Service
public class UserService {
    @AutoWired
    SQLManager sqlManager;

    public String get() {
        return "hello service!";
    }

    public int save() {
        User paras = new User();
//        paras.setId(2);
        paras.setName("1");
        paras.setAge(11);
        int a = sqlManager.insert(User.class, paras);
        return a;
    }

    public List query() {
        String sql = "select * from user where id=#{id} and name=#{name}";
        User paras = new User();
        paras.setId(1);
        paras.setName("1");
        List<User> list = sqlManager.execute(sql, User.class, paras);
        return list;
    }
}
