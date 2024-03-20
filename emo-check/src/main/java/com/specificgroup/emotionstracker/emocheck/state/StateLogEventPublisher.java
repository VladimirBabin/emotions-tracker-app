package com.specificgroup.emotionstracker.emocheck.state;

import com.specificgroup.emotionstracker.emocheck.state.domain.Emotion;
import com.specificgroup.emotionstracker.emocheck.state.domain.StateLog;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Publisher of state logged events based on AmqpTemplate high-level implementation.
 */
@Service
public class StateLogEventPublisher {
    private final AmqpTemplate amqpTemplate;
    private final String stateLogTopicExchange;
    private final String emotionsTopicExchange;
    private final String triggeringStateRoutingKey;
    private final String triggeringEmotionRoutingKey;


    public StateLogEventPublisher(AmqpTemplate amqpTemplate,
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

    public void stateLogged(StateLog stateLog) {
        StateLoggedEvent stateLoggedEvent = buildStateLoggedEvent(stateLog);
        amqpTemplate.convertAndSend(stateLogTopicExchange, stateLoggedEvent);
        if (stateLog.getState().isAlertTriggering()) {
            amqpTemplate.convertAndSend(stateLogTopicExchange,
                    triggeringStateRoutingKey,
                    stateLoggedEvent);
        }

        if (stateLog.getEmotions() != null) {
            publishEmotions(stateLog);
        }
    }

    /**
     * Publishes emotion logged events for every separate emotion.
     * Publishes triggering emotions with a separate routing key.
     * @param stateLog StateLog object.
     */
    private void publishEmotions(StateLog stateLog) {
        stateLog.getEmotions().forEach(emotion -> {
            EmotionLoggedEvent emotionLoggedEvent = buildEmotionLoggedEvent(stateLog, emotion);
            amqpTemplate.convertAndSend(emotionsTopicExchange, emotionLoggedEvent);

            if (emotion.isAlertTriggering()) {
                amqpTemplate.convertAndSend(emotionsTopicExchange,
                        triggeringEmotionRoutingKey,
                        emotionLoggedEvent);
            }
        });
    }

    private EmotionLoggedEvent buildEmotionLoggedEvent(StateLog stateLog, Emotion emotion) {
        return new EmotionLoggedEvent(stateLog.getId(),
                stateLog.getUser().getId(),
                stateLog.getUser().getAlias(),
                emotion,
                stateLog.getDateTime());
    }

    private StateLoggedEvent buildStateLoggedEvent(StateLog stateLog) {
        return new StateLoggedEvent(stateLog.getId(),
                stateLog.getUser().getId(),
                stateLog.getUser().getAlias(),
                stateLog.getState(),
                stateLog.getDateTime());
    }
}
