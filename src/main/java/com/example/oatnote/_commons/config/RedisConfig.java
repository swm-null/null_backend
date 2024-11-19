package com.example.oatnote._commons.config;

import org.redisson.api.RedissonClient;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.beans.factory.annotation.Value;

import com.example.oatnote.domain.sse.SseSubscriber;

@Configuration
public class RedisConfig {

    @Value("${sse.topic}")
    private String topic;

    @Bean
    public RedisMessageListenerContainer container(RedissonClient redissonClient, MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(new RedissonConnectionFactory(redissonClient));
        container.addMessageListener(listenerAdapter, new ChannelTopic(topic));
        return container;
    }

    @Bean
    public MessageListenerAdapter listenerAdapter(SseSubscriber subscriber) {
        return new MessageListenerAdapter(subscriber, "onMessage");
    }

    @Bean
    public ChannelTopic topic() {
        return new ChannelTopic(topic);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedissonClient redissonClient) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(new RedissonConnectionFactory(redissonClient));
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }
}
