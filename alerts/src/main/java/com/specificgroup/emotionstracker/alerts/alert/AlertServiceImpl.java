package com.specificgroup.emotionstracker.alerts.alert;

import com.specificgroup.emotionstracker.alerts.alert.emotionalertprocessors.EmotionAlertProcessor;
import com.specificgroup.emotionstracker.alerts.alert.emotionalertprocessors.EmotionAlertSecondaryProcessor;
import com.specificgroup.emotionstracker.alerts.alert.statealertprocessors.StateAlertProcessor;
import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlert;
import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType;
import com.specificgroup.emotionstracker.alerts.alert.domain.StateAlert;
import com.specificgroup.emotionstracker.alerts.alert.domain.StateAlertType;
import com.specificgroup.emotionstracker.alerts.state.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlertServiceImpl implements AlertService {
    private final StateLogRepository stateLogRepository;
    private final EmotionLogRepository emotionLogRepository;
    private final StateAlertRepository stateAlertRepository;
    private final EmotionAlertRepository emotionAlertRepository;
    private final List<EmotionAlertProcessor> emotionAlertProcessors;
    private final List<EmotionAlertSecondaryProcessor> emotionAlertSecondaryProcessors;
    private final List<StateAlertProcessor> stateAlertProcessors;

    @Override
    public List<StateAlertType> getLastMinuteAddedStateAlerts(Long userId) {
        return stateAlertRepository
                .getAlertTypesByUserIdAfterGivenLocalDateTime(userId, LocalDateTime.now().minusMinutes(1));
    }

    @Override
    public List<EmotionAlertType> getLastMinuteAddedEmotionAlerts(Long userId) {
        return emotionAlertRepository
                .getAlertTypesByUserIdAfterGivenLocalDateTime(userId, LocalDateTime.now().minusMinutes(1));
    }

    @Override
    public void newTriggeringStateForUser(StateLoggedEvent event) {
        stateLogRepository.save(new StateLog(null,
                event.getUserId(),
                event.getState(),
                event.getDateTime()));

        List<StateAlert> stateAlerts = processForStateAlerts(event);

        if (!stateAlerts.isEmpty()) {
            // make a WebSocket call to frontend
        }
    }

    private List<StateAlert> processForStateAlerts(StateLoggedEvent event) {
        // get all logged triggering states for user
        List<StateLog> userStateLogs = stateLogRepository
                .findByUserOrderByDateTime(event.getUserId());

        List<StateAlert> userStateAlertsForLastMonth = stateAlertRepository.getAlertsByUserIdAfterGivenLocalDateTime(
                event.getUserId(), LocalDateTime.now().minusDays(30));

        // check if user is eligible for new alerts, persist them and return
        List<StateAlert> newStateAlerts = stateAlertProcessors.stream()
                .map(p -> p.processForOptionalAlert(userStateLogs, userStateAlertsForLastMonth))
                .flatMap(Optional::stream)
                .map(stateAlertType -> new StateAlert(event.getUserId(), stateAlertType))
                .toList();

        stateAlertRepository.saveAll(newStateAlerts);

        return newStateAlerts;
    }


    @Override
    public void newTriggeringEmotionForUser(EmotionLoggedEvent event) {
        emotionLogRepository.save(new EmotionLog(null,
                event.getUserId(),
                event.getEmotion(),
                event.getDateTime()));

        List<EmotionAlert> emotionAlerts = processForEmotionAlerts(event);

        if (!emotionAlerts.isEmpty()) {
            // make a WebSocket call to frontend
        }
    }

    private List<EmotionAlert> processForEmotionAlerts(EmotionLoggedEvent event) {
        // get all logs for user for particular emotion
        List<EmotionLog> userEmotionLogs = emotionLogRepository
                .findByUserIdAndEmotionOrderByDateTime(event.getUserId(), event.getEmotion());

        List<EmotionAlert> userEmotionAlertsForLastMonth = emotionAlertRepository.getAlertsByUserIdAfterGivenLocalDateTime(
                event.getUserId(), LocalDateTime.now().minusDays(30));

        // check if user is eligible for new alerts, persist them and return
        List<EmotionAlert> newEmotionAlerts = new ArrayList<>(emotionAlertProcessors.stream()
                .map(p -> p.processForOptionalAlert(userEmotionLogs, userEmotionAlertsForLastMonth))
                .flatMap(Optional::stream)
                .map(emotionAlertType -> new EmotionAlert(event.getUserId(), emotionAlertType))
                .toList());

        if (!newEmotionAlerts.isEmpty()) {
            List<EmotionAlert> secondaryAlerts = emotionAlertSecondaryProcessors.stream()
                    .map(p -> p.processForOptionalAlert(userEmotionAlertsForLastMonth))
                    .flatMap(Optional::stream)
                    .map(emotionAlertType -> new EmotionAlert(event.getUserId(), emotionAlertType))
                    .toList();
            newEmotionAlerts.addAll(secondaryAlerts);
        }

        emotionAlertRepository.saveAll(newEmotionAlerts);

        return newEmotionAlerts;
    }
}
