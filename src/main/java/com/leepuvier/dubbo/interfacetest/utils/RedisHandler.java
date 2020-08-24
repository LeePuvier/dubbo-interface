package com.leepuvier.dubbo.interfacetest.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Author : LeePuvier
 * @CreateTime : 2020/6/29  9:27 PM
 * @ContentUse :
 */

@Slf4j
@Configuration
public class RedisHandler {

    @Resource
    RedisTemplate redisTemplate;

    public long ttl(String key) {
        return redisTemplate.getExpire(key);
    }

    public void expire(String key, long timeout) {
        redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
    }

    public long incr(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    public long decr(String key, long delta) {
        if(delta<0){
            del(key);
            return 0;
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    public void del(String key) {
        redisTemplate.delete(key);
    }

    public void set(Object key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, String value, long timeout) {
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.SECONDS);
    }

    public void hset(String key, String field, Object value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    public void hdel(String key, Object... fields) {
        redisTemplate.opsForHash().delete(key, fields);
    }

    public Map<Object, Object> hgetall(String key) {
        return redisTemplate.opsForHash().entries(key);
    }

    public long lpush(String key, String value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    public String lpop(String key) {
        return (String) redisTemplate.opsForList().leftPop(key);
    }

    public long rpush(String key, String value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    public void storeToken(String key, String value, long expireTime, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, expireTime, unit);
    }

    public String getToken(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    public boolean refreshTokenLifeTime(String key, long lifeTime, TimeUnit unit) {
        return redisTemplate.expire(key, lifeTime, unit);
    }

    public boolean isExpire(String key) {
        long lifeTime = 0L;
        lifeTime = redisTemplate.getExpire(key);
        log.debug("token key[" + key + "] lifeTime : " + lifeTime);
        if (lifeTime < 0) {
            return true;
        }
        return false;
    }

    public String get(String key) {
        String result = null;
        try {
            result = redisTemplate.opsForValue().get(key).toString();
        }catch (NullPointerException e)
        {
            return null;
        }catch (Exception ee){
            ee.printStackTrace();
        }
        return result;
    }

    public String getNull(String key) {
        String result = null;
        try {
            result = redisTemplate.opsForValue().get(key).toString();
        }catch (NullPointerException e)
        {
            return null;
        }catch (Exception ee){
            ee.printStackTrace();
        }
        return result;
    }

    public String hget(String key, String field) {
        String result = null;
        try {
            result = redisTemplate.opsForHash().get(key, field).toString();
        }catch (NullPointerException e){
            return null;
        }catch (Exception ee){
            ee.printStackTrace();
        }
        return result;
    }
}
