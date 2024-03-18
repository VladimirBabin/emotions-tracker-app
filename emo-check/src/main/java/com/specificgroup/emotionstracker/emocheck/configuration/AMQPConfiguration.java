package com.specificgroup.emotionstracker.emocheck.configuration;

import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configures RabbitMQ via AMQP abstraction to implement event-driven architecture.
 */
@Configuration
public class AMQPConfiguration {

    @Bean
    public TopicExchange stateLogsTopicExchange(@Value("${amqp.exchange.states}") String exchangeName) {
        return ExchangeBuilder.topicExchange(exchangeName).durable(true).build();
    }

    @Bean
    public TopicExchange emotionsTopicExchange(@Value("${amqp.exchange.emotions}") String exchangeName) {
        return ExchangeBuilder.topicExchange(exchangeName).durable(true).build();
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
