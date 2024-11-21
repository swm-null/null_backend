package com.example.oatnote.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
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
    private String fileDeleteQueueName;

    @Value("${spring.rabbitmq.queues.file.delete.routing-key}")
    private String fileDeleteRoutingKey;

    @Value("${spring.rabbitmq.queues.file.delete-all.queue}")
    private String fileDeleteAllQueueName;

    @Value("${spring.rabbitmq.queues.file.delete-all.routing-key}")
    private String fileDeleteAllRoutingKey;

    @Value("${spring.rabbitmq.queues.sse.exchange}")
    private String sseExchangeName;

    @Value("${spring.rabbitmq.queues.sse.send-processing-memos-count.queue}")
    private String sseSendProcessingMemosCountQueueName;

    @Value("${spring.rabbitmq.queues.sse.send-processing-memos-count.routing-key}")
    private String sseSendProcessingMemosCountRoutingKey;

    @Value("${spring.rabbitmq.queues.dlx.exchange}")
    private String dlxExchangeName;

    @Value("${spring.rabbitmq.queues.dlx.queue}")
    private String dlxQueueName;

    @Value("${spring.rabbitmq.queues.dlx.routing-key}")
    private String dlxRoutingKey;

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
    public DirectExchange fileExchange() {
        return new DirectExchange(fileExchangeName, true, false);
    }

    @Bean
    public Queue fileDeleteQueue() {
        return new Queue(fileDeleteQueueName, true);
    }

    @Bean
    public Binding fileDeleteBinding(Queue fileDeleteQueue, DirectExchange fileExchange) {
        return BindingBuilder.bind(fileDeleteQueue)
            .to(fileExchange)
            .with(fileDeleteRoutingKey);
    }

    @Bean
    public Queue fileDeleteAllQueue() {
        return new Queue(fileDeleteAllQueueName, true);
    }

    @Bean
    public Binding fileDeleteAllBinding(Queue fileDeleteAllQueue, DirectExchange fileExchange) {
        return BindingBuilder.bind(fileDeleteAllQueue)
            .to(fileExchange)
            .with(fileDeleteAllRoutingKey);
    }

    @Bean
    public DirectExchange sseExchange() {
        return new DirectExchange(sseExchangeName, true, false);
    }

    @Bean
    public Queue sseSendProcessingMemosCountQueue() {
        return new Queue(sseSendProcessingMemosCountQueueName, true);
    }

    @Bean
    public Binding sseSendProcessingMemosCountBinding(Queue sseSendProcessingMemosCountQueue, DirectExchange sseExchange) {
        return BindingBuilder.bind(sseSendProcessingMemosCountQueue)
            .to(sseExchange)
            .with(sseSendProcessingMemosCountRoutingKey);
    }

    @Bean
    public DirectExchange dlxExchange() {
        return new DirectExchange(dlxExchangeName, true, false);
    }

    @Bean
    public Queue dlxQueue() {
        return new Queue(dlxQueueName, true);
    }

    @Bean
    public Binding dlxBinding(Queue dlxQueue, DirectExchange dlxExchange) {
        return BindingBuilder.bind(dlxQueue)
            .to(dlxExchange)
            .with(dlxRoutingKey);
    }
}
