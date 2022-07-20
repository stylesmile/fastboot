package com.example;

import io.github.stylesmile.annotation.AutoWired;
import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.ioc.Value;
import io.github.stylesmile.tool.PropertyUtil;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.List;

@Controller
public class TestController {

    @AutoWired
    Jedis jedis;

    @Value(value = "fast.name")
    private String name;

    @RequestMapping("/")
    public String hello() {
        return "hello fastboot";
    }

    @RequestMapping("/1")
    public String hello1() {
        return jedis.set("test", "hello");
    }

    @RequestMapping("/2")
    public String hello2() {
        return jedis.get("test");
    }

}
