package io.github.stylesmile.jedis;

import io.github.stylesmile.ioc.BeanContainer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * redis帮助类，有两种方式，不同的存储方式，获取时要选择一致的获取方式。 1：直接使用String类型进行数据存储
 * 2：使用byte[]序列化方式进行数据存储
 *
 * @author Stylesmile
 */
public class JedisTemplate {

    /**
     * 获取jedis
     */
    private static Jedis getJedis() {
        JedisPool jedisPool = BeanContainer.getInstance(JedisPool.class);
        return jedisPool.getResource();
    }

    /**
     * 设置缓存
     *
     * @param key   缓存key
     * @param value 缓存value
     */
    public void set(String key, String value) {
        try (Jedis jedis = getJedis()) {
            jedis.set(key, value);
        }
    }


    /**
     * 设置缓存，并且自己指定过期时间
     *
     * @param key        键
     * @param value      值
     * @param expireTime 过期时间
     */
    public void setex(String key, String value, int expireTime) {
        try (Jedis jedis = getJedis()) {
            jedis.setex(key, expireTime, value);
        }
    }

    /**
     * 获取指定key的缓存
     *
     * @param key 键
     */
    public String get(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.get(key);
        }
    }

    /**
     * 删除缓存
     */
    public Boolean delete(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.del(key) > 0;
        }
    }

    /**
     * key 值是否存在
     */
    public Boolean existKey(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.exists(key);
        }
    }

    /**
     * 递增操作
     *
     * @param key 键
     * @param by  步长
     * @return Double
     */
    public Double incr(String key, double by) {
        try (Jedis jedis = getJedis()) {
            return jedis.incrByFloat(key, by);
        }
    }

    /**
     * 模糊查询keys
     *
     * @param pattern 匹配值
     * @return Set
     */
    @Deprecated
    public Set<String> keys(String pattern) {
        try (Jedis jedis = getJedis()) {
            return jedis.hkeys(pattern);
        }
    }

    /*
     * ==================================== 序列化方式存储数据
     * ====================================
     */

    /**
     * 设置缓存,序列化方式
     *
     * @param key    缓存key
     * @param object 缓存value
     */
    public void setSerializeData(String key, Object object) {
        try (Jedis jedis = getJedis()) {
            jedis.set(GsonByteUtils.toByteArray(key), GsonByteUtils.toByteArray(object));
        }
    }

    /**
     * 设置缓存，Serialize方式，并且自己指定过期时间
     *
     * @param key        键
     * @param value      值
     * @param expireTime 过期时间
     */
    public void setSerializeDataEx(String key, Object value, int expireTime) {
        try (Jedis jedis = getJedis()) {
            jedis.setex(GsonByteUtils.toByteArray(key), expireTime, GsonByteUtils.toByteArray(value));
        }
    }

    /**
     * 获取指定key的缓存,Serialize 方式
     *
     * @param key   键
     * @param clazz 类
     */
    public <T> T getSerializeData(String key, Class clazz) {
        T t = null;
        try {
            byte[] value;
            try (Jedis jedis = getJedis()) {
                value = jedis.get(GsonByteUtils.toByteArray(key));
            }
            if (null == value || value.length == 0) {
                return t;
            }
            try {
                t = (T) GsonByteUtils.fromByteArray(value, clazz);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return t;
    }

    /**
     * 设置缓存,序列化方式
     *
     * @param key   缓存key
     * @param value 缓存value
     */
    public void hsetSerializeData(String key, String field, Object value) {
        try (Jedis jedis = getJedis()) {
            jedis.hset(GsonByteUtils.toByteArray(key), GsonByteUtils.toByteArray(field), GsonByteUtils.toByteArray(value));
        }
    }


    /**
     * 删除指定key field的缓存,Serialize 方式
     *
     * @param key 键
     */
    public void hdelSerializeData(String key, String field) {
        try (Jedis jedis = getJedis()) {
            jedis.hdel(GsonByteUtils.toByteArray(key), GsonByteUtils.toByteArray(field));
        }
    }

    /**
     * 删除缓存 Serialize方式
     *
     * @param key 键
     * @return Boolean
     */
    public Boolean deleteSerializeData(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.del(GsonByteUtils.toByteArray(key)) > 0;
        }
    }

    /**
     * key 值是否存在 Serialize方式
     *
     * @param key 键
     * @return Boolean
     */
    public Boolean existSerializeDataKey(String key) {
        try (Jedis jedis = getJedis()) {
            return jedis.exists(GsonByteUtils.toByteArray(key));
        }
    }

    /**
     * 递增操作 Serialize方式
     *
     * @param key 键
     * @param by  步长
     * @return Double
     */
    public Double incrSerializeData(String key, double by) {
        try (Jedis jedis = getJedis()) {
            return jedis.incrByFloat(GsonByteUtils.toByteArray(key), by);
        }
    }

    /**
     * 递增操作 Serialize方式
     *
     * @param key 键
     * @param by  步长
     * @return long
     */
    public long incrSerializeLongData(String key, long by) {
        try (Jedis jedis = getJedis()) {
            return jedis.incrBy(GsonByteUtils.toByteArray(key), by);
        }
    }

    public synchronized long incrSerializeLongDataUpdate(String key, long by) {
        try (Jedis jedis = getJedis()) {
            return jedis.incrBy(GsonByteUtils.toByteArray(key), by);
        }
    }

    /**
     * 递增操作 Serialize方式
     *
     * @param key 键
     * @param by  步长
     * @return long
     */
    public long incrLongData(String key, long by) {
        try (Jedis jedis = getJedis()) {
            return jedis.incrBy(key, by);
        }
    }

    /**
     * 模糊查询keys Serialize方式
     */
    public Set<byte[]> keysSerializeData(String pattern) {
        try (Jedis jedis = getJedis()) {
            return jedis.hkeys(GsonByteUtils.toByteArray(pattern));
        }
    }

    /**
     * 设置过期时间
     *
     * @param key               键
     * @param expireTimeSeconds expireTime 过期时间
     */
    public void setExpire(String key, int expireTimeSeconds) {
        try (Jedis jedis = getJedis()) {
            jedis.expire(key, expireTimeSeconds);
        }
    }

    /**
     * 设置serialize过期时间
     *
     * @param key               键
     * @param expireTimeSeconds expireTime 过期时间
     */
    public void setSerializeExpire(String key, int expireTimeSeconds) {
        try (Jedis jedis = getJedis()) {
            jedis.expire(GsonByteUtils.toByteArray(key), expireTimeSeconds);
        }
    }

    /**
     * 递减数据并获取指定key field的缓存
     *
     * @param key   键
     * @param value 键
     */
    public long decrbyGetData(String key, long value) {
        try (Jedis jedis = getJedis()) {
            return jedis.decrBy(key, value);
        }
    }

    /**
     * 向set中添加元素
     *
     * @param key   键
     * @param value 值
     */
    public void setAdd(String key, String value) {
        try (Jedis jedis = getJedis()) {
            jedis.sadd(key, value);
        }
    }

    /**
     * 判断set中是否包含元素
     *
     * @param key   键
     * @param value 值
     * @return boolean
     */
    public boolean setExistMember(String key, String value) {
        try (Jedis jedis = getJedis()) {
            return jedis.sismember(key, value);
        }
    }

}
