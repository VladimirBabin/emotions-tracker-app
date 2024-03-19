package com.specificgroup.emotionstracker.alerts.alert;

import com.specificgroup.emotionstracker.alerts.alert.AlertService;
import com.specificgroup.emotionstracker.alerts.state.EmotionLoggedEvent;
import com.specificgroup.emotionstracker.alerts.state.StateLoggedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * Handles logged state and emotion events to create alerts when alert is triggered.
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class AlertEventHandler {

    private final AlertService alertService;

    @RabbitListener(queues = "${amqp.queue.alert-states}")
    void handleNewStateLogged(StateLoggedEvent stateLoggedEvent) {
        log.info("State Logged Event received: {}", stateLoggedEvent.getState());
        try {
            alertService.newTriggeringStateForUser(stateLoggedEvent);
        } catch (Exception e) {
            log.error("Error when trying to process ChallengeSolvedEvent", e);
            // Avoids the event to be re-queues and reprocessed.
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }

    @RabbitListener(queues = "${amqp.queue.alert-emotions}")
    void handleNewEmotionLogged(EmotionLoggedEvent emotionLoggedEvent) {
        log.info("Emotion Logged Event received: {}", emotionLoggedEvent.getEmotion());
        try {
            alertService.newTriggeringEmotionForUser(emotionLoggedEvent);
        } catch (Exception e) {
            log.error("Error when trying to process ChallengeSolvedEvent", e);
            // Avoids the event to be re-queues and reprocessed.
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
}
