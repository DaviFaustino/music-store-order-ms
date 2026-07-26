package com.davifaustino.musicstore.order.infrastructure.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String PRODUCT_CREATED_QUEUE_NAME = "order.product-created.queue";

    @Bean
    public Queue productCreatedQueue() {
        return new Queue(PRODUCT_CREATED_QUEUE_NAME, true);
    }
    
    @Bean
    public JacksonJsonMessageConverter jacksonJsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}
