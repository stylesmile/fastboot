package com.example;

import io.github.stylesmile.annotation.AutoWired;
import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.ioc.Value;
import io.github.stylesmile.jedis.JedisTemplate;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

@Controller
public class TestController {

    @AutoWired
    Jedis jedis;

    @AutoWired
    JedisTemplate jedisTemplate;

    @Value(value = "fast.name")
    private String name;

    @RequestMapping("/0")
    public String hello() {
        jedis.set("test", "hello");
        return jedis.get("test");
    }

    @RequestMapping("/1")
    public String hello1() {
        jedis.set("test1", "hello");
        String s = jedis.get("test1");
        jedisTemplate.set("test2", "hello");
        String s2 = jedisTemplate.get("test2");
        return s2;
    }

    @RequestMapping("/2")
    public String hello2() {
        return jedis.get("test");
    }

    @RequestMapping("/3")
    public String hello3() {
        jedisTemplate.rpushString("trusteeship", "test");
        return jedisTemplate.rpopString("trusteeship");
    }

    @RequestMapping("/4")
    public Map hello4() {
        Map map = new HashMap<>();
        map.put("test",1);
        jedisTemplate.rpushSerializeData("testrpush", map);
        return jedisTemplate.rpopSerializeData("testrpush",Map.class);
    }

}
