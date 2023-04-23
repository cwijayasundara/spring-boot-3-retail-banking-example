package com.cham.customercore.redis;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
@Configuration
public class RedisProperties {
    private final int redisPort;
    private final String redisHost;
    public RedisProperties(@Value("${spring.redis.port}") final int redisPort, @Value("${spring.redis.host}") final String redisHost) {
        this.redisPort = redisPort;
        this.redisHost = redisHost;
    }
    public int getRedisPort() {
        return redisPort;
    }
    public String getRedisHost() {
        return redisHost;
    }
}