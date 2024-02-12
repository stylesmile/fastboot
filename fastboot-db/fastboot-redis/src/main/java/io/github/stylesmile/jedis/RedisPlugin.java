package io.github.stylesmile.jedis;

import io.github.stylesmile.ioc.BeanContainer;
import io.github.stylesmile.plugin.Plugin;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPooled;

/**
 * @author Stylesmile
 */
public class RedisPlugin implements Plugin {


    @Override
    public void start() {
    }

    @Override
    public void init() {
        JedisPool jedisPool = JedisConfig.getJedisPool();
        BeanContainer.setInstance(JedisPool.class, jedisPool);
//        Jedis jedis = JedisConfig.getJedisPool().getResource();
        JedisTemplate jedisTemplate = new JedisTemplate();
        BeanContainer.setInstance(JedisTemplate.class, jedisTemplate);
    }

    @Override
    public void end() {

    }

}
