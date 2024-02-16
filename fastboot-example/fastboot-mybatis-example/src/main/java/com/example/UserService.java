package com.example;

import com.example.mapper.UserMapper;
import io.github.stylesmile.annotation.AutoWired;
import io.github.stylesmile.annotation.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class UserService {
    @AutoWired
    UserMapper userMapper;

    public String get() {
        return "hello service!";
    }

    public List query() {
        List list = userMapper.selectByMap(new HashMap<>(1));
        System.out.println(list);
        return list;
    }

    public List query2() {
        List list = userMapper.queryUser();
        System.out.println(list);
        return list;
    }

    public int insert() {
        User user = new User();
        user.setAge(18);
        user.setName("Stylesmile"+System.currentTimeMillis());
        int i = userMapper.insert(user);
        return i;
    }
}
