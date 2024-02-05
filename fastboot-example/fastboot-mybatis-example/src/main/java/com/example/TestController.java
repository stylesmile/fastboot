package com.example;

import io.github.stylesmile.annotation.AutoWired;
import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.ioc.Value;

import java.util.List;

@Controller
public class TestController {
    @AutoWired
    UserService userService;

    @Value(value = "fast.name")
    private String name;

    @RequestMapping("/")
    public String hello() {
        return "hello fastboot";
    }

    @RequestMapping("/2")
    public String hello2() {
        return userService.get();
    }

    @RequestMapping("/3")
    public List<User> hello3() {
        return userService.query();
    }

    @RequestMapping("/4")
    public List<User> hello4() {
        return userService.query2();
    }
}
