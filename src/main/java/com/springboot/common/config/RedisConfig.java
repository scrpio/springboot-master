package com.springboot.common.config;

import com.springboot.common.constant.CommonConstant;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {
    @Bean
    public JedisPool jedisPool() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(1000);
        jedisPoolConfig.setMaxWaitMillis(10000);
        jedisPoolConfig.setMaxIdle(200);
        return new JedisPool(jedisPoolConfig, CommonConstant.REDIS_HOST, CommonConstant.REDIS_PORT, 1000, CommonConstant.REDIS_PASSWORD);
    }
}
