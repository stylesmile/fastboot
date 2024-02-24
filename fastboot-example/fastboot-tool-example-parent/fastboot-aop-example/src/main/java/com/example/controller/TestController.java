package com.example.controller;

import com.example.aop.LoginFilter;
import com.example.service.TestService;
import io.github.stylesmile.annotation.AutoWired;
import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.ioc.Value;

@Controller
public class TestController {

    @AutoWired
    TestService testService;

    @Value(value = "fast.name")
    private String name;

    @LoginFilter
    @RequestMapping("/hello")
    public String hello() {
        System.out.println(name);
        return testService.getHello();
    }


}
