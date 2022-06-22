package com.example.demo.test;

import com.example.demo.Controller;
import com.example.demo.RequestMapping;
import com.example.demo.RequestMethod;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class UserController {
    @Resource
    private IUserService userService;

    @RequestMapping(value = "/userList", method = RequestMethod.GET)
    public List<User> getUserList() {
        List<User> userList = userService.getAllUser();
        return userList;
    }
} 