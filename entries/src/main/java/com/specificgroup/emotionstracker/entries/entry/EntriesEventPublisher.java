package com.specificgroup.emotionstracker.entries.entry;

import com.specificgroup.emotionstracker.entries.entry.domain.Emotion;
import com.specificgroup.emotionstracker.entries.entry.domain.Entry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Publisher of state logged events based on AmqpTemplate high-level implementation.
 */
@Service
@Slf4j
public class EntriesEventPublisher {
    private final AmqpTemplate amqpTemplate;
    private final String statesTopicExchange;
    private final String emotionsTopicExchange;
    private final String triggeringStateRoutingKey;
    private final String triggeringEmotionRoutingKey;
    private final String nonTriggeringStateRoutingKey;
    private final String nonTriggeringEmotionRoutingKey;
    private final String removedTopicExchange;


    public EntriesEventPublisher(AmqpTemplate amqpTemplate,
                                 @Value("${amqp.exchange.states}") String statesTopicExchange,
                                 @Value("${amqp.exchange.emotions}") String emotionsTopicExchange,
                                 @Value("${amqp.routing-key.triggering-state}") String triggeringStateRoutingKey,
                                 @Value("${amqp.routing-key.triggering-emotion}") String triggeringEmotionRoutingKey,
                                 @Value("${amqp.routing-key.non-triggering-state}") String nonTriggeringStateRoutingKey,
                                 @Value("${amqp.routing-key.non-triggering-emotion}") String nonTriggeringEmotionRoutingKey,
                                 @Value("${amqp.exchange.removed-entry}") String removedTopicExchange) {
        this.amqpTemplate = amqpTemplate;
        this.statesTopicExchange = statesTopicExchange;
        this.emotionsTopicExchange = emotionsTopicExchange;
        this.triggeringStateRoutingKey = triggeringStateRoutingKey;
        this.triggeringEmotionRoutingKey = triggeringEmotionRoutingKey;
        this.nonTriggeringStateRoutingKey = nonTriggeringStateRoutingKey;
        this.nonTriggeringEmotionRoutingKey = nonTriggeringEmotionRoutingKey;
        this.removedTopicExchange = removedTopicExchange;
    }

    /**
     * Publishes state logged events with a corresponding routing key.
     *
     * @param entry Entry object.
     */
    public void entryLogged(Entry entry) {
        StateLoggedEvent stateLoggedEvent = buildStateLoggedEvent(entry);

        log.info("Publisher: handling event with id {}", stateLoggedEvent.getEntryId());

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
     * Publishes emotion logged events with a corresponding routing key
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

    /**
     * Informs that entry was removed
     * @param entryId id of removed entry.
     */
    public void entryRemoved(Long entryId) {
        amqpTemplate.convertAndSend(removedTopicExchange, "", entryId);
    }
}
