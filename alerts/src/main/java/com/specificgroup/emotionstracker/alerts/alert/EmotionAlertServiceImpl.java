package com.specificgroup.emotionstracker.alerts.alert;

import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlert;
import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType;
import com.specificgroup.emotionstracker.alerts.alert.emotionalertprocessors.EmotionAlertProcessor;
import com.specificgroup.emotionstracker.alerts.alert.emotionalertprocessors.EmotionAlertSecondaryProcessor;
import com.specificgroup.emotionstracker.alerts.state.EmotionLog;
import com.specificgroup.emotionstracker.alerts.state.EmotionLogRepository;
import com.specificgroup.emotionstracker.alerts.state.EmotionLoggedEvent;
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
public class EmotionAlertServiceImpl implements EmotionAlertService {
    public static final int DAYS_BEFORE_ALERT_CAN_REPEAT = 30;
    private final EmotionLogRepository logRepository;
    private final EmotionAlertRepository alertRepository;
    private final List<EmotionAlertProcessor> alertProcessors;
    private final List<EmotionAlertSecondaryProcessor> secondaryProcessors;

    @Override
    public List<EmotionAlertType> getLastMinuteAddedEmotionAlerts(Long userId) {
        return null;
    }

    @Override
    public void newTriggeringEmotionForUser(EmotionLoggedEvent event) {
        logRepository.save(new EmotionLog(null,
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
        List<EmotionLog> userEmotionLogs = logRepository
                .findByUserIdAndEmotionOrderByDateTime(event.getUserId(), event.getEmotion());

        List<EmotionAlert> latestAlerts = alertRepository.getAlertsByUserIdAfterGivenLocalDateTime(
                event.getUserId(), LocalDateTime.now().minusDays(DAYS_BEFORE_ALERT_CAN_REPEAT));

        // check if user is eligible for new alerts, persist them and return
        List<EmotionAlert> newEmotionAlerts = new ArrayList<>(alertProcessors.stream()
                .map(p -> p.processForOptionalAlert(userEmotionLogs, latestAlerts))
                .flatMap(Optional::stream)
                .map(emotionAlertType -> new EmotionAlert(event.getUserId(), emotionAlertType))
                .toList());

        if (!newEmotionAlerts.isEmpty()) {
            List<EmotionAlert> secondaryAlerts = secondaryProcessors.stream()
                    .map(p -> p.processForOptionalAlert(latestAlerts))
                    .flatMap(Optional::stream)
                    .map(emotionAlertType -> new EmotionAlert(event.getUserId(), emotionAlertType))
                    .toList();
            newEmotionAlerts.addAll(secondaryAlerts);
        }

        if (!newEmotionAlerts.isEmpty()) {
            alertRepository.saveAll(newEmotionAlerts);
        }

        return newEmotionAlerts;
    }
}
