package com.example.oatnote._commons.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.queues.file.exchange}")
    private String fileExchangeName;

    @Value("${spring.rabbitmq.queues.file.delete.queue}")
    private String deleteFileQueueName;

    @Value("${spring.rabbitmq.queues.file.delete.routing-key}")
    private String deleteFileRoutingKey;

    @Value("${spring.rabbitmq.queues.file.delete-user-all.queue}")
    private String deleteUserAllFilesQueueName;

    @Value("${spring.rabbitmq.queues.file.delete-user-all.routing-key}")
    private String deleteUserAllFilesRoutingKey;

    @Value("${spring.rabbitmq.queues.dlx.exchange}")
    private String dlxExchangeName;

    @Value("${spring.rabbitmq.queues.dlx.routing-key}")
    private String dlxRoutingKey;

    @Value("${spring.rabbitmq.queues.dlx.queue}")
    private String dlxQueueName;

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        return factory;
    }

    @Bean
    public TopicExchange fileTopicExchange() {
        return new TopicExchange(fileExchangeName, true, false);
    }

    @Bean
    public Queue deleteFileQueue() {
        return new Queue(deleteFileQueueName, true);
    }

    @Bean
    public Queue deleteUserAllFilesQueue() {
        return new Queue(deleteUserAllFilesQueueName, true);
    }

    @Bean
    public Binding deleteFileBinding(Queue deleteFileQueue, TopicExchange fileTopicExchange) {
        return BindingBuilder.bind(deleteFileQueue)
            .to(fileTopicExchange)
            .with(deleteFileRoutingKey);
    }

    @Bean
    public Binding deleteUserAllFilesBinding(Queue deleteUserAllFilesQueue, TopicExchange fileTopicExchange) {
        return BindingBuilder.bind(deleteUserAllFilesQueue)
            .to(fileTopicExchange)
            .with(deleteUserAllFilesRoutingKey);
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
