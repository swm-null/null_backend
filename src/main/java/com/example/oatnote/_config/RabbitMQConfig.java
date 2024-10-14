package com.example.oatnote._config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.queue}")
    private String queueName;

    @Value("${spring.rabbitmq.exchange}")
    private String exchangeName;

    @Value("${spring.rabbitmq.routing-key}")
    private String routingKey;

    @Value("${spring.rabbitmq.dlx.exchange}")
    private String dlxExchangeName;

    @Value("${spring.rabbitmq.dlx.queue}")
    private String dlxQueueName;

    @Value("${spring.rabbitmq.dlx.routing-key}")
    private String dlxRoutingKey;

    @Bean
    public Queue memoQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange(exchangeName, true, false);
    }

    @Bean
    public Binding binding(Queue memoQueue, TopicExchange topicExchange) {
        return BindingBuilder.bind(memoQueue)
            .to(topicExchange)
            .with(routingKey);
    }

    @Bean
    public Queue dlxQueue() {
        return new Queue(dlxQueueName, true);
    }

    @Bean
    public TopicExchange dlxExchange() {
        return new TopicExchange(dlxExchangeName, true, false);
    }

    @Bean
    public Binding dlxBinding(Queue dlxQueue, TopicExchange dlxExchange) {
        return BindingBuilder.bind(dlxQueue)
            .to(dlxExchange)
            .with(dlxRoutingKey);
    }
}
