package com.example.oatnote._commons.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.queues.file.exchange}")
    private String fileExchangeName;

    @Value("${spring.rabbitmq.queues.file.routing-key}")
    private String fileRoutingKey;

    @Value("${spring.rabbitmq.queues.file.queue}")
    private String fileQueueName;

    @Value("${spring.rabbitmq.queues.user.exchange}")
    private String userExchangeName;

    @Value("${spring.rabbitmq.queues.user.routing-key}")
    private String userRoutingKey;

    @Value("${spring.rabbitmq.queues.user.queue}")
    private String userQueueName;

    @Value("${spring.rabbitmq.queues.dlx.exchange}")
    private String dlxExchangeName;

    @Value("${spring.rabbitmq.queues.dlx.routing-key}")
    private String dlxRoutingKey;

    @Value("${spring.rabbitmq.queues.dlx.queue}")
    private String dlxQueueName;

    @Bean
    public TopicExchange fileTopicExchange() {
        return new TopicExchange(fileExchangeName, true, false);
    }

    @Bean
    public Queue fileQueue() {
        return new Queue(fileQueueName, true);
    }

    @Bean
    public Binding fileBinding(Queue fileQueue, TopicExchange fileTopicExchange) {
        return BindingBuilder.bind(fileQueue)
            .to(fileTopicExchange)
            .with(fileRoutingKey);
    }

    @Bean
    public TopicExchange userTopicExchange() {
        return new TopicExchange(userExchangeName, true, false);
    }

    @Bean
    public Queue userQueue() {
        return new Queue(userQueueName, true);
    }

    @Bean
    public Binding userBinding(Queue userQueue, TopicExchange userTopicExchange) {
        return BindingBuilder.bind(userQueue)
            .to(userTopicExchange)
            .with(userRoutingKey);
    }

    @Bean
    public TopicExchange dlxTopicExchange() {
        return new TopicExchange(dlxExchangeName, true, false);
    }

    @Bean
    public Queue dlxQueue() {
        return new Queue(dlxQueueName, true);
    }

    @Bean
    public Binding dlxBinding(Queue dlxQueue, TopicExchange dlxTopicExchange) {
        return BindingBuilder.bind(dlxQueue)
            .to(dlxTopicExchange)
            .with(dlxRoutingKey);
    }
}
