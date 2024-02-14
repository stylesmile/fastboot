package com.example.controller;

import com.example.aop2.LoginFilter;
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

    @RequestMapping("/")
    public String hello() {
        return testService.getHello();
    }
    @RequestMapping("/1")
    public String hello1() {
        System.out.println(name);
        return testService.getHello();
    }
    @LoginFilter
    @RequestMapping("/2")
    public String hello2() {
        System.out.println(name);
        return testService.getHello();
    }


}
