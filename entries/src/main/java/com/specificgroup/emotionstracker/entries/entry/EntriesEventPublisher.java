package com.specificgroup.emotionstracker.entries.entry;

import com.specificgroup.emotionstracker.entries.entry.domain.Emotion;
import com.specificgroup.emotionstracker.entries.entry.domain.Entry;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Publisher of state logged events based on AmqpTemplate high-level implementation.
 */
@Service
public class EntriesEventPublisher {
    private final AmqpTemplate amqpTemplate;
    private final String stateLogTopicExchange;
    private final String emotionsTopicExchange;
    private final String triggeringStateRoutingKey;
    private final String triggeringEmotionRoutingKey;


    public EntriesEventPublisher(AmqpTemplate amqpTemplate,
                                 @Value("${amqp.exchange.states}") String stateLogTopicExchange,
                                 @Value("${amqp.exchange.emotions}") String emotionsTopicExchange,
                                 @Value("${amqp.routing-key.triggering-state}") String triggeringStateRoutingKey,
                                 @Value("${amqp.routing-key.triggering-emotion}") String triggeringEmotionRoutingKey) {
        this.amqpTemplate = amqpTemplate;
        this.stateLogTopicExchange = stateLogTopicExchange;
        this.emotionsTopicExchange = emotionsTopicExchange;
        this.triggeringStateRoutingKey = triggeringStateRoutingKey;
        this.triggeringEmotionRoutingKey = triggeringEmotionRoutingKey;
    }

    public void stateLogged(Entry entry) {
        StateLoggedEvent stateLoggedEvent = buildStateLoggedEvent(entry);
        amqpTemplate.convertAndSend(stateLogTopicExchange, stateLoggedEvent);
        if (entry.getState().isAlertTriggering()) {
            amqpTemplate.convertAndSend(stateLogTopicExchange,
                    triggeringStateRoutingKey,
                    stateLoggedEvent);
        }

        if (entry.getEmotions() != null) {
            emotionsLogged(entry);
        }
    }

    /**
     * Publishes emotion logged events for every separate emotion.
     * Publishes triggering emotions with a separate routing key.
     * @param entry EntryLog object.
     */
    private void emotionsLogged(Entry entry) {
        entry.getEmotions().forEach(emotion -> {
            EmotionLoggedEvent emotionLoggedEvent = buildEmotionLoggedEvent(entry, emotion);
            amqpTemplate.convertAndSend(emotionsTopicExchange, emotionLoggedEvent);

            if (emotion.isAlertTriggering()) {
                amqpTemplate.convertAndSend(emotionsTopicExchange,
                        triggeringEmotionRoutingKey,
                        emotionLoggedEvent);
            }
        });
    }

    private EmotionLoggedEvent buildEmotionLoggedEvent(Entry entry, Emotion emotion) {
        return new EmotionLoggedEvent(entry.getId(),
                entry.getUserId(),
                emotion,
                entry.getDateTime());
    }

    private StateLoggedEvent buildStateLoggedEvent(Entry entry) {
        return new StateLoggedEvent(entry.getId(),
                entry.getUserId(),
                entry.getState(),
                entry.getDateTime());
    }
}
