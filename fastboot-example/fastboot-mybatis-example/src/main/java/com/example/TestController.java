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

    @RequestMapping("/1")
    public List<User> hello3() {
        return userService.query();
    }

    @RequestMapping("/2")
    public Integer hello5() {
        return userService.insert();
    }
}
