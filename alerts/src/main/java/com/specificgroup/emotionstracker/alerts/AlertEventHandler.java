package com.specificgroup.emotionstracker.alerts;

import com.specificgroup.emotionstracker.alerts.state.EmotionLoggedEvent;
import com.specificgroup.emotionstracker.alerts.state.StateLoggedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
        alertService.newStateLogForUser(stateLoggedEvent);
    }

    @RabbitListener(queues = "${amqp.queue.alert-emotions}")
    void handleNewEmotionLogged(EmotionLoggedEvent emotionLoggedEvent) {
        log.info("Emotion Logged Event received: {}", emotionLoggedEvent.getEmotion());
        alertService.newEmotionLogForUser(emotionLoggedEvent);
    }
}
