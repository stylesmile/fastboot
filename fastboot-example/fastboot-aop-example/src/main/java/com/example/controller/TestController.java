package com.example.controller;

import com.example.service.TestService;
import io.github.stylesmile.annotation.AutoWired;
import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.ioc.Value;

@Controller
public class TestController {

    @AutoWired
    TestService testService;

    @Value(value = "fast.name")
    private String name;

    public String hello() {
        return testService.getHello();
    }


}
