package com.specificgroup.emotionstracker.stats.stats;


import com.specificgroup.emotionstracker.stats.entry.EmotionLoggedEvent;
import com.specificgroup.emotionstracker.stats.entry.StateLoggedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * Handles logged state and emotion events to update statistics.
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class StatsEventHandler {

    private final StateStatsService stateService;
    private final EmotionStatsService emotionService;

    @RabbitListener(queues = "${amqp.queue.states}")
    public void handleNewStateLogged(StateLoggedEvent event) {
        log.info("State logged event received for user {} and state: {}", event.getUserId(), event.getState());
        try {
            stateService.newStateLogForUser(event);
        } catch (Exception e) {
            log.error("Error when trying to process StateLoggedEvent", e);
            // Avoids the event to be re-queues and reprocessed.
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }

    @RabbitListener(queues = "${amqp.queue.emotions}")
    public void handleNewEmotionLogged(EmotionLoggedEvent event) {
        log.info("Emotion logged event received for user {} and emotion: {}", event.getUserId(), event.getEmotion());
        try {
            emotionService.newEmotionLogForUser(event);
        } catch (Exception e) {
            log.error("Error when trying to process EmotionLoggedEvent", e);
            // Avoids the event to be re-queues and reprocessed.
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }

    @RabbitListener(queues = "${amqp.queue.removed-entry}")
    void handleRemovedEntry(Long entryId) {
        log.info("Entry {} removed", entryId);
        try {
            emotionService.removeEntryRelatedData(entryId);
            stateService.removeEntryRelatedData(entryId);
        } catch (Exception e) {
            log.error("Error when removing entry related data", e);
            // Avoids the event to be re-queues and reprocessed.
            throw new AmqpRejectAndDontRequeueException(e);
        }
    }
}
