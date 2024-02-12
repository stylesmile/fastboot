package io.github.stylesmile;


import io.github.stylesmile.app.App;
import io.github.stylesmile.jedis.JedisTemplate;

import javax.annotation.Resource;

/**
 * 测试入口类
 *
 * @author stylesmile
 */
public class Application {
    @Resource
    JedisTemplate jedisTemplate;

    public static void main(String[] args) {
        App.start(Application.class, args);
    }
}
