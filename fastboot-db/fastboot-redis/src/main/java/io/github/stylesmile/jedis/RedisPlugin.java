package io.github.stylesmile.jedis;

import io.github.stylesmile.ioc.BeanContainer;
import io.github.stylesmile.plugin.Plugin;
import redis.clients.jedis.Jedis;

/**
 * @author Stylesmile
 */
public class RedisPlugin implements Plugin {


    @Override
    public void start() {
    }

    @Override
    public void init() {
        Jedis jedis = RedisConfig.getJedisPoolConfig();
        BeanContainer.setInstance(Jedis.class, jedis);
    }

    @Override
    public void end() {

    }

}
