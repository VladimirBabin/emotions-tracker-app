package com.specificgroup.emotionstracker.alerts.alert;

import com.specificgroup.emotionstracker.alerts.entry.EmotionLoggedEvent;
import com.specificgroup.emotionstracker.alerts.entry.StateLoggedEvent;
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
    private final StateAlertService stateAlertService;
    private final EmotionAlertService emotionAlertService;

    @RabbitListener(queues = "${amqp.queue.alert-states}")
    void handleNewStateLogged(StateLoggedEvent stateLoggedEvent) {
        log.info("State Logged Event received: {}", stateLoggedEvent.getState());
        try {
            stateAlertService.newTriggeringStateForUser(stateLoggedEvent);
        } catch (Exception e) {
            log.error("Error when trying to process StateLoggedEvent", e);
            // Avoids the event to be re-queues and reprocessed.
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }

    @RabbitListener(queues = "${amqp.queue.alert-emotions}")
    void handleNewEmotionLogged(EmotionLoggedEvent emotionLoggedEvent) {
        log.info("Emotion Logged Event received: {}", emotionLoggedEvent.getEmotion());
        try {
            emotionAlertService.newTriggeringEmotionForUser(emotionLoggedEvent);
        } catch (Exception e) {
            log.error("Error when trying to process StateLoggedEvent", e);
            // Avoids the event to be re-queues and reprocessed.
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }

    @RabbitListener(queues = "${amqp.queue.removed-entry}")
    void handleRemovedEntry(Long entryId) {
        log.info("Entry {} removed", entryId);
        try {
            emotionAlertService.removeEntryRelatedData(entryId);
            stateAlertService.removeEntryRelatedData(entryId);
        } catch (Exception e) {
            log.error("Error when removing entry related data", e);
            // Avoids the event to be re-queues and reprocessed.
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
}
