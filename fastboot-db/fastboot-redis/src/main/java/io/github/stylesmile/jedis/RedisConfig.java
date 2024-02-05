package io.github.stylesmile.jedis;

import io.github.stylesmile.tool.PropertyUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConfig {
    public static Jedis getJedisPoolConfig() {
        String host = PropertyUtil.getProperty("redis.host");
        String port = PropertyUtil.getProperty("redis.port");
        String db = PropertyUtil.getProperty("redis.db");
        String password = PropertyUtil.getProperty("redis.password");
        String timeout = PropertyUtil.getProperty("redis.timeout");
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//        jedisPoolConfig.setMaxIdle(maxIdle);
//        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        JedisPool jedisPool = new JedisPool(
                jedisPoolConfig,
                host,
                Integer.parseInt(port),
                Integer.parseInt(timeout),
                password, db);
        Jedis jedis = jedisPool.getResource();
        return jedis;
    }
}
