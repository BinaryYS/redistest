package com.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.util.ResourceBundle;

/**
 * Created by song.yang on 2017/3/29  0:06
 * <p>
 * blog:https://binaryys.github.io/
 */
public class RedisProvider {
    protected static final Logger logger = LoggerFactory.getLogger(RedisProvider.class);
    protected static JedisPool jedispool;
    protected static int EXPIRE = 130;
    static{
        ResourceBundle bundle = ResourceBundle.getBundle("config");
        if (bundle == null) {
            throw new IllegalArgumentException(
                    "[redis.properties] is not found!");
        }

        EXPIRE = Integer.valueOf(bundle.getString("redis.expire"));

        JedisPoolConfig jedisconfig = new JedisPoolConfig();
        jedisconfig.setMaxActive(Integer.valueOf(bundle
                .getString("redis.pool.maxActive")));
        jedisconfig.setMaxIdle(Integer.valueOf(bundle
                .getString("redis.pool.maxIdle")));
        jedisconfig.setMaxWait(Long.valueOf(bundle
                .getString("redis.pool.maxWait")));
        jedisconfig.setTestOnBorrow(Boolean.valueOf(bundle
                .getString("redis.pool.testOnBorrow")));
        jedisconfig.setTestOnReturn(Boolean.valueOf(bundle
                .getString("redis.pool.testOnReturn")));
        jedispool = new JedisPool(jedisconfig, bundle.getString("redis.ip"),
                Integer.valueOf(bundle.getString("redis.port")), 100000);
    }

    public static Jedis getJedis() {
        Jedis jedis = null;
        try {
            jedis = jedispool.getResource();
        } catch (JedisConnectionException jce) {
            logger.info("exception:[{}]", jce.getMessage());
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                logger.info("exception:[{}]", e.getMessage());
            }
            jedis = jedispool.getResource();
        }
        return jedis;
    }

    public static void returnResource(JedisPool pool, Jedis jedis) {
        if (jedis != null) {
            pool.returnResource(jedis);
        }
    }
}
