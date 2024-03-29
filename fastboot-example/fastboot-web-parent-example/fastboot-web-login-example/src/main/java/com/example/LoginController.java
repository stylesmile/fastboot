package com.example;

import com.example.entity.User;
import com.example.result.Result;
import com.example.tool.MD5Util;
import io.github.stylesmile.annotation.AutoWired;
import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.RequestMapping;
import io.github.stylesmile.jedis.JedisTemplate;

@Controller
public class LoginController {
    @AutoWired
    JedisTemplate jedisTemplate;


    @RequestMapping("/login")
    public Result<String> login(String username, String password) {
        if (("admin").equals(username) && ("123456").equals(password)) {
            User user = User.builder()
                    .username("admin")
                    .age("18")
                    .build();
            //返回token
            String token = MD5Util.calculateMD5(username+System.currentTimeMillis());
            // 设置缓存，600秒
            jedisTemplate.setSerializeDataEx(
                    String.format("user:user_info_%s", token),
                    user, 600);
            return Result.success(token);
        } else {
            return Result.failMessage("fail");
        }
    }

    @RequestMapping("/user")
    public Result<User> user() {
        User user = User.builder()
                .username("admin")
                .age("18")
                .build();
        return Result.success(user);
    }
}
