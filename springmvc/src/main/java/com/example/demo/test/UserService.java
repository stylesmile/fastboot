package com.example.demo.test;

import com.example.demo.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements IUserService {

    /**
     * 获取所有用户
     */
    public List<User> getAllUser() {
        List<User> userList = new ArrayList<>();
        userList.add(new User(1, "Jack", 22));
        userList.add(new User(2, "Rouce", 23));
        userList.add(new User(3, "Lisa", 32));
        return userList;
    }
}