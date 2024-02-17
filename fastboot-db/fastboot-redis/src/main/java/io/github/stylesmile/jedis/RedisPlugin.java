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
        // 获取jedis 连接池
        JedisPool jedisPool = JedisConfig.getJedisPool();
        // 加入到 bean 中
        BeanContainer.setInstance(JedisPool.class, jedisPool);
        BeanContainer.setInstance(Jedis.class, jedisPool.getResource());
        // 获取 jedis 操作模板
        JedisTemplate jedisTemplate = new JedisTemplate();
        // 加入到 bean 中
        BeanContainer.setInstance(JedisTemplate.class, jedisTemplate);
    }

    @Override
    public void end() {

    }

}
