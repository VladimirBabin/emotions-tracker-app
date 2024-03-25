package com.specificgroup.emotionstracker.entrylogging.state;

import com.specificgroup.emotionstracker.entrylogging.state.domain.Emotion;
import com.specificgroup.emotionstracker.entrylogging.state.domain.EntryLog;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Publisher of state logged events based on AmqpTemplate high-level implementation.
 */
@Service
public class EntryLogEventPublisher {
    private final AmqpTemplate amqpTemplate;
    private final String stateLogTopicExchange;
    private final String emotionsTopicExchange;
    private final String triggeringStateRoutingKey;
    private final String triggeringEmotionRoutingKey;


    public EntryLogEventPublisher(AmqpTemplate amqpTemplate,
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

    public void stateLogged(EntryLog entryLog) {
        StateLoggedEvent stateLoggedEvent = buildStateLoggedEvent(entryLog);
        amqpTemplate.convertAndSend(stateLogTopicExchange, stateLoggedEvent);
        if (entryLog.getState().isAlertTriggering()) {
            amqpTemplate.convertAndSend(stateLogTopicExchange,
                    triggeringStateRoutingKey,
                    stateLoggedEvent);
        }

        if (entryLog.getEmotions() != null) {
            emotionsLogged(entryLog);
        }
    }

    /**
     * Publishes emotion logged events for every separate emotion.
     * Publishes triggering emotions with a separate routing key.
     * @param entryLog EntryLog object.
     */
    private void emotionsLogged(EntryLog entryLog) {
        entryLog.getEmotions().forEach(emotion -> {
            EmotionLoggedEvent emotionLoggedEvent = buildEmotionLoggedEvent(entryLog, emotion);
            amqpTemplate.convertAndSend(emotionsTopicExchange, emotionLoggedEvent);

            if (emotion.isAlertTriggering()) {
                amqpTemplate.convertAndSend(emotionsTopicExchange,
                        triggeringEmotionRoutingKey,
                        emotionLoggedEvent);
            }
        });
    }

    private EmotionLoggedEvent buildEmotionLoggedEvent(EntryLog entryLog, Emotion emotion) {
        return new EmotionLoggedEvent(entryLog.getId(),
                entryLog.getUserId(),
                emotion,
                entryLog.getDateTime());
    }

    private StateLoggedEvent buildStateLoggedEvent(EntryLog entryLog) {
        return new StateLoggedEvent(entryLog.getId(),
                entryLog.getUserId(),
                entryLog.getState(),
                entryLog.getDateTime());
    }
}
