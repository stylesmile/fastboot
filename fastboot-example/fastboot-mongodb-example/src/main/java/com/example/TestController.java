package com.example;

import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.ioc.Value;
import io.github.stylesmile.tool.PropertyUtil;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class TestController {
    @Resource
    UserService userService;

    @Value(value = "fast.name")
    private String name;

    @RequestMapping("/")
    public String hello() {
        return "hello fastboot";
    }


    @RequestMapping("/1")
    public String hello1() {
        return userService.save();
    }

    @RequestMapping("/2")
    public List<User> hello2() {
        return userService.query();
    }

}
