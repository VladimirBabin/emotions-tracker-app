package com.specificgroup.emotionstracker.alerts.configuration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.RabbitListenerConfigurer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.DefaultMessageHandlerMethodFactory;
import org.springframework.messaging.handler.annotation.support.MessageHandlerMethodFactory;

@Configuration
public class AMQPConfiguration {

    @Value("${amqp.exchange.states}")
    String stateLogsExchangeName;

    @Value("${amqp.exchange.emotions}")
    String emotionsExchangeName;

    @Value("${amqp.queue.alert-states}")
    String alertStateQueueName;

    @Value("${amqp.queue.alert-emotions}")
    String alertEmotionsQueueName;

    @Value("${amqp.routing-key.triggering-state}")
    String triggeringStateRoutingKey;

    @Value("${amqp.routing-key.triggering-emotion}")
    String triggeringEmotionRoutingKey;

    @Bean
    public TopicExchange stateLogsTopicExchange() {
        return ExchangeBuilder.topicExchange(stateLogsExchangeName).durable(true).build();
    }

    @Bean
    public TopicExchange emotionsTopicExchange() {
        return ExchangeBuilder.topicExchange(emotionsExchangeName).durable(true).build();
    }

    @Bean
    public Queue alertStatesQueue() {
        return QueueBuilder.durable(alertStateQueueName).build();
    }

    @Bean
    public Queue alertEmotionsQueue() {
        return QueueBuilder.durable(alertEmotionsQueueName).build();
    }

    @Bean
    public Binding triggeringStates() {
        return BindingBuilder.bind(alertStatesQueue())
                .to(stateLogsTopicExchange())
                .with(triggeringStateRoutingKey);
    }

    @Bean
    public Binding triggeringEmotions() {
        return BindingBuilder.bind(alertEmotionsQueue())
                .to(emotionsTopicExchange())
                .with(triggeringEmotionRoutingKey);
    }

    @Bean
    public MessageHandlerMethodFactory messageHandlerMethodFactory() {
        DefaultMessageHandlerMethodFactory factory = new
                DefaultMessageHandlerMethodFactory();
        final MappingJackson2MessageConverter jsonConverter =
                new MappingJackson2MessageConverter();
        jsonConverter.getObjectMapper().registerModule(
                new ParameterNamesModule(JsonCreator.Mode.PROPERTIES));
        jsonConverter.getObjectMapper().registerModule(new JavaTimeModule());
        factory.setMessageConverter(jsonConverter);
        return factory;
    }

    @Bean
    public RabbitListenerConfigurer rabbitListenerConfigurer(
            final MessageHandlerMethodFactory messageHandlerMethodFactory) {
        return c -> c.setMessageHandlerMethodFactory(messageHandlerMethodFactory);
    }
}
