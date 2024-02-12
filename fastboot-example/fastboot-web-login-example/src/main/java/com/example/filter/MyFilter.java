package com.example.filter;

import com.example.entity.User;
import io.github.stylesmile.annotation.AutoWired;
import io.github.stylesmile.annotation.Controller;
import io.github.stylesmile.annotation.Service;
import io.github.stylesmile.filter.Filter;
import io.github.stylesmile.jedis.JedisTemplate;
import io.github.stylesmile.server.Request;
import io.github.stylesmile.server.Response;

@Service
public class MyFilter implements Filter {
    @AutoWired
    JedisTemplate jedisTemplate;

    @Override
    public boolean preHandle(Request request, Response response) {
        String uri = request.getPath();
        System.out.println("uri: " + uri);
        if (uri.equals("/login")) {
            return true;
        }
        String token = request.getHeaders()
                .get("token");
        System.out.println("token: " + token);
        User user = jedisTemplate.getSerializeData("user:user_info", User.class);
        if (user != null) {
            return true;
        }
        throw new RuntimeException("用户无权限");
    }

    @Override
    public boolean afterCompletion(Request request, Response response) {
        return true;
    }
}
