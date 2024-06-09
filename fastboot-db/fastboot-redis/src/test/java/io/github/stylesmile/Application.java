package io.github.stylesmile;


import io.github.stylesmile.annotation.AutoWired;
import io.github.stylesmile.app.App;
import io.github.stylesmile.jedis.JedisTemplate;


/**
 * 测试入口类
 *
 * @author stylesmile
 */
public class Application {
    @AutoWired
    JedisTemplate jedisTemplate;

    public static void main(String[] args) {
        App.start(Application.class, args);
    }
}
