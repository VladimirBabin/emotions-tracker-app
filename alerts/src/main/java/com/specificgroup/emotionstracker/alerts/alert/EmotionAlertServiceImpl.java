package com.specificgroup.emotionstracker.alerts.alert;

import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlert;
import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType;
import com.specificgroup.emotionstracker.alerts.alert.emotionalertprocessors.EmotionAlertProcessor;
import com.specificgroup.emotionstracker.alerts.entry.EmotionEntry;
import com.specificgroup.emotionstracker.alerts.entry.EmotionEntryRepository;
import com.specificgroup.emotionstracker.alerts.entry.EmotionLoggedEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmotionAlertServiceImpl implements EmotionAlertService {
    public static final int DAYS_BEFORE_ALERT_CAN_REPEAT = 30;
    private static final int MINUTES_SPAN_FOR_RECENT_ALERT = 60;
    private final EmotionEntryRepository entryRepository;
    private final EmotionAlertRepository alertRepository;
    private final List<EmotionAlertProcessor> alertProcessors;

    @Override
    public List<String> getLastAddedEmotionAlerts(String userId) {
        List<EmotionAlert> recentAlertsForUser = alertRepository
                .getNotShownAlertsByUserIdAfterGivenLocalDateTime(userId,
                        LocalDateTime.now().minusMinutes(MINUTES_SPAN_FOR_RECENT_ALERT));
        if (!recentAlertsForUser.isEmpty()) {
            alertRepository.saveAll(recentAlertsForUser.stream().map(e ->
                            new EmotionAlert(e.getAlertId(), e.getUserId(),
                                    e.getLocalDateTime(), e.getEmotionAlertType(), true))
                    .toList());
        }

        return recentAlertsForUser.stream()
                .map(EmotionAlert::getEmotionAlertType)
                .map(EmotionAlertType::getDescription)
                .toList();
    }

    @Transactional
    @Override
    public void newTriggeringEmotionForUser(EmotionLoggedEvent event) {
        entryRepository.save(new EmotionEntry(null,
                event.getEntryId(),
                event.getUserId(),
                event.getEmotion(),
                event.getDateTime()));

        processForEmotionAlerts(event);
    }

    @Transactional
    @Override
    public void removeEntryRelatedData(Long entryId) {
        entryRepository.deleteAllByEntryId(entryId);
    }

    private void processForEmotionAlerts(EmotionLoggedEvent event) {
        // get all logs for user for particular emotion
        List<EmotionEntry> userEmotionEntries = entryRepository
                .findByUserIdAndEmotionOrderByDateTime(event.getUserId(), event.getEmotion());

        List<EmotionAlert> latestAlerts = alertRepository.getAlertsByUserIdAfterGivenLocalDateTime(
                event.getUserId(), LocalDateTime.now().minusDays(DAYS_BEFORE_ALERT_CAN_REPEAT));

        // check if user is eligible for new alerts, persist them and return
        List<EmotionAlert> newEmotionAlerts = alertProcessors.stream()
                .filter(p -> p.getEmotionType().equals(event.getEmotion()))
                .map(p -> p.processForOptionalAlertWithCheck(userEmotionEntries, latestAlerts))
                .flatMap(Optional::stream)
                .map(emotionAlertType -> new EmotionAlert(event.getUserId(), emotionAlertType))
                .toList();

        if (!newEmotionAlerts.isEmpty()) {
            alertRepository.saveAll(newEmotionAlerts);
        }
    }
}
