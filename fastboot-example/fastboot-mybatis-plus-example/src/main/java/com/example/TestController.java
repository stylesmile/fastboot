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

    @AutoWired
    UserService2 userService2;

    @Value(value = "fast.name")
    private String name;

    @RequestMapping("/1")
    public List<User> hello1() {
        return userService.query();
    }

    @RequestMapping("/3")
    public List<User> hello2() {
        return userService.query2();
    }

    @RequestMapping("/3")
    public Integer hello3() {
        return userService.insert();
    }

    @RequestMapping("/4")
    public List<User> hello4() {
        return userService2.selectAll();
    }
    @RequestMapping("/5")
    public Boolean hello5() {
        User user = new User();
        user.setAge(18);
        user.setName("Stylesmile"+System.currentTimeMillis());
        boolean b = user.insert();
        System.out.println(b);
        return b;
    }
}
