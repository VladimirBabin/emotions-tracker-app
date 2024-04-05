package com.specificgroup.emotionstracker.stats.configuration;

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

    @Value("${amqp.exchange.removed-entry}")
    String removedEntriesExchange;

    @Value("${amqp.queue.states}")
    String statesQueueName;

    @Value("${amqp.queue.emotions}")
    String emotionsQueueName;

    @Value("${amqp.queue.removed-entry}")
    String removedEntriesQueueName;

    @Bean
    public TopicExchange statesTopicExchange() {
        return ExchangeBuilder.topicExchange(stateLogsExchangeName).durable(true).build();
    }

    @Bean
    public TopicExchange emotionsTopicExchange() {
        return ExchangeBuilder.topicExchange(emotionsExchangeName).durable(true).build();
    }

    @Bean
    public FanoutExchange removedEntriesExchange() {
        return ExchangeBuilder.fanoutExchange(removedEntriesExchange).durable(true).build();
    }

    @Bean
    public Queue statesQueue() {
        return QueueBuilder.durable(statesQueueName).build();
    }

    @Bean
    public Queue emotionsQueue() {
        return QueueBuilder.durable(emotionsQueueName).build();
    }

    @Bean
    public Queue removedEntriesQueue() {
        return QueueBuilder.durable(removedEntriesQueueName).build();
    }


    @Bean
    public Binding triggeringStates() {
        return BindingBuilder.bind(statesQueue())
                .to(statesTopicExchange())
                .with("#");
    }

    @Bean
    public Binding triggeringEmotions() {
        return BindingBuilder.bind(emotionsQueue())
                .to(emotionsTopicExchange())
                .with("#");
    }

    @Bean
    public Binding removedEntries() {
        return BindingBuilder.bind(removedEntriesQueue())
                .to(removedEntriesExchange());
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
