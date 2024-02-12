package com.example.filter;

import com.example.entity.User;
import io.github.stylesmile.annotation.AutoWired;
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
        User user = jedisTemplate.getSerializeData(String.format("user:user_info_%s", token), User.class);
        if (user != null) {
            return true;
        }
        System.err.println("用户无权限 " + uri);
//        throw new RuntimeException("用户无权限 " + uri);
        return false;
    }

    @Override
    public boolean afterCompletion(Request request, Response response) {
        return true;
    }
}
