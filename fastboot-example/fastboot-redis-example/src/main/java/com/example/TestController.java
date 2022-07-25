package com.example;

import io.github.stylesmile.annotation.AutoWired;
import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.ioc.Value;
import redis.clients.jedis.Jedis;

@Controller
public class TestController {

    @AutoWired
    Jedis jedis;

    @Value(value = "fast.name")
    private String name;

    @RequestMapping("/")
    public String hello() {
        jedis.set("test", "hello");
        return jedis.get("test");
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
