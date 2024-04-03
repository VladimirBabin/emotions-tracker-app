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
    private final String statesTopicExchange;
    private final String emotionsTopicExchange;
    private final String triggeringStateRoutingKey;
    private final String triggeringEmotionRoutingKey;
    private final String nonTriggeringStateRoutingKey;
    private final String nonTriggeringEmotionRoutingKey;


    public EntriesEventPublisher(AmqpTemplate amqpTemplate,
                                 @Value("${amqp.exchange.states}") String statesTopicExchange,
                                 @Value("${amqp.exchange.emotions}") String emotionsTopicExchange,
                                 @Value("${amqp.routing-key.triggering-state}") String triggeringStateRoutingKey,
                                 @Value("${amqp.routing-key.triggering-emotion}") String triggeringEmotionRoutingKey,
                                 @Value("${amqp.routing-key.non-triggering-state}") String nonTriggeringStateRoutingKey,
                                 @Value("${amqp.routing-key.non-triggering-emotion}") String nonTriggeringEmotionRoutingKey) {
        this.amqpTemplate = amqpTemplate;
        this.statesTopicExchange = statesTopicExchange;
        this.emotionsTopicExchange = emotionsTopicExchange;
        this.triggeringStateRoutingKey = triggeringStateRoutingKey;
        this.triggeringEmotionRoutingKey = triggeringEmotionRoutingKey;
        this.nonTriggeringStateRoutingKey = nonTriggeringStateRoutingKey;
        this.nonTriggeringEmotionRoutingKey = nonTriggeringEmotionRoutingKey;
    }

    public void stateLogged(Entry entry) {
        StateLoggedEvent stateLoggedEvent = buildStateLoggedEvent(entry);

        amqpTemplate.convertAndSend(statesTopicExchange,
                entry.getState().isAlertTriggering() ?
                        triggeringStateRoutingKey :
                        nonTriggeringStateRoutingKey,
                stateLoggedEvent);


        if (entry.getEmotions() != null) {
            emotionsLogged(entry);
        }
    }

    /**
     * Publishes emotion logged events for every separate emotion.
     * Publishes triggering emotions with a separate routing key.
     *
     * @param entry Entry object.
     */
    private void emotionsLogged(Entry entry) {
        entry.getEmotions().forEach(emotion -> {
            EmotionLoggedEvent emotionLoggedEvent = buildEmotionLoggedEvent(entry, emotion);

            amqpTemplate.convertAndSend(emotionsTopicExchange,
                    emotion.isAlertTriggering() ?
                    triggeringEmotionRoutingKey :
                    nonTriggeringEmotionRoutingKey,
                    emotionLoggedEvent);

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
