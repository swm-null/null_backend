package com.example.oatnote._commons.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    private static final String REDISSION_HOST_FORMAT = "redis://%s:%d";

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.password}")
    private String redisPassword;

    @Bean
    public RedissonClient redissionClient() {
        Config config = new Config();
        String address = String.format(REDISSION_HOST_FORMAT, redisHost, redisPort);
        config.useSingleServer()
            .setAddress(address)
            .setPassword(redisPassword);
        return Redisson.create(config);
    }
}
