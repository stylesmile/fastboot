package com.example;

import com.example.mapper.UserMapper;
import io.github.stylesmile.annotation.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Service
public class UserService {
    @Resource
    UserMapper userMapper;
    public String get() {
        return "hello service!";
    }

    public List query() {
        List list =  userMapper.selectByMap(new HashMap<>(1));
        System.out.println(list);
        return  list;
    }
}
