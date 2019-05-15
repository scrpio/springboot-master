package com.springboot.service.impl;

import com.springboot.service.IRedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.util.Slowlog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class RedisServiceImpl implements IRedisService {
    @Autowired
    private JedisPool jedisPool;

    @Override
    public String set(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.set(key, value);
        jedis.close();
        return result;
    }

    @Override
    public String set(byte[] key, byte[] value) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.set(key, value);
        jedis.close();
        return result;
    }

    @Override
    public Map<String, Object> getMap(String key) {
        Jedis jedis = jedisPool.getResource();
        Map<String, Object> result = new HashMap<>();
        result.put("key", key);
        result.put("value", jedis.get(key));
        result.put("type", jedis.type(key));
        result.put("expire", jedis.ttl(key));
        result.put("size", jedis.get(key).getBytes().length);
        jedis.close();
        return result;
    }

    @Override
    public Long del(String key) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.del(key);
        jedis.close();
        return result;
    }

    @Override
    public String info() {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.info();
        jedis.close();
        return result;
    }

    @Override
    public Set<String> keys(String pattern) {
        Jedis jedis = jedisPool.getResource();
        Set<String> result = jedis.keys(pattern);
        jedis.close();
        return result;
    }

    @Override
    public Long keysCount() {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.dbSize();
        jedis.close();
        return result;
    }

    @Override
    public String flushAll() {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.flushAll();
        jedis.close();
        return result;
    }

    @Override
    public String get(String key) {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.get(key);
        jedis.close();
        return result;
    }

    @Override
    public byte[] get(byte[] key) {
        Jedis jedis = jedisPool.getResource();
        byte[] result = jedis.get(key);
        jedis.close();
        return result;
    }

    @Override
    public List<Slowlog> slowlogGet() {
        Jedis jedis = jedisPool.getResource();
        List<Slowlog> result = jedis.slowlogGet();
        jedis.close();
        return result;
    }

    @Override
    public String logEmpty() {
        Jedis jedis = jedisPool.getResource();
        String result = jedis.slowlogReset();
        jedis.close();
        return result;
    }

    public Long expire(String key, int seconds) {
        Jedis jedis = jedisPool.getResource();
        Long result = jedis.expire(key, seconds);
        jedis.close();
        return result;
    }
}
