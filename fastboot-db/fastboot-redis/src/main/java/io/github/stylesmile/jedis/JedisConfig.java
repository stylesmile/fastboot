package io.github.stylesmile.jedis;

import io.github.stylesmile.tool.PropertyUtil;
import io.github.stylesmile.tool.StringUtil;
import redis.clients.jedis.*;

/**
 * redis 配置
 */
public class JedisConfig {

    /**
     * jedis 连接池
     * @return JedisPool
     */
    static JedisPool getJedisPool() {
        String host = PropertyUtil.getProperty("redis.host");
        String port = PropertyUtil.getProperty("redis.port");
        String user = PropertyUtil.getProperty("redis.user");
        String db = PropertyUtil.getProperty("redis.db");
        String password = PropertyUtil.getProperty("redis.password");
        String timeout = PropertyUtil.getProperty("redis.timeout");
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//        jedisPoolConfig.setMaxIdle(maxIdle);
//        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);

        return new JedisPool(
                jedisPoolConfig,
                host,
                Integer.parseInt(port),
                Integer.parseInt(timeout),
                user,
                password,
                Integer.parseInt(db)
        );
    }
}
