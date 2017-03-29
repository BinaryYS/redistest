package com.redis;

/**
 * Created by song.yang on 2017/3/29  0:17
 * <p>
 * blog:https://binaryys.github.io/
 */
public class RedisTest {
    public static void main(String[] args) {
        RedisHelper redisHelper = new RedisHelper();
        System.out.println(redisHelper.get("name"));
        System.out.println(redisHelper.get("e-mail"));
    }
}
