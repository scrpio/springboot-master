package com.springboot.service;

import redis.clients.util.Slowlog;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface IRedisService {
    String set(String key, String value);

    String set(byte[] key, byte[] value);

    Map<String, Object> getMap(String key);

    Long del(String key);

    String info();

    Set<String> keys(String pattern);

    Long keysCount();

    String flushAll();

    String get(String key);

    byte[] get(byte[] key);

    List<Slowlog> slowlogGet();

    String logEmpty();

    Long expire(String key, int seconds);
}
