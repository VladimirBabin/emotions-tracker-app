package com.specificgroup.emotionstracker.alerts.alert.emotionalertprocessors;

import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlert;
import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType;
import com.specificgroup.emotionstracker.alerts.entry.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmotionAlertProcessor {
    /**
     * Checks if the user's last logged emotion should trigger an alert.
     * @return an EmotionAlertType triggered by the emotion logged.
     */
    Optional<EmotionAlertType> processForOptionalAlert(List<EmotionEntry> allLoggedEmotions,
                                                       List<EmotionAlert> lastEmotionAlerts);

    default Optional<EmotionAlertType> processForOptionalAlertWithCheck(List<EmotionEntry> allLoggedEmotions,
                                                                List<EmotionAlert> lastEmotionAlerts) {
        if (userAlreadyReceivedAlertInCustomPeriod(lastEmotionAlerts)) {
            return Optional.empty();
        }
        return this.processForOptionalAlert(allLoggedEmotions, lastEmotionAlerts);
    }

    int checkedDaysPeriod();

    private boolean userAlreadyReceivedAlertInCustomPeriod(List<EmotionAlert> lastEmotionAlerts) {
        return lastEmotionAlerts.stream()
                .filter(ea -> ea.getEmotionAlertType().equals(emotionAlertType()))
                .anyMatch(ea -> ea.getLocalDateTime().isAfter(LocalDateTime.now().minusDays(checkedDaysPeriod())));
    }

    EmotionAlertType emotionAlertType();
    Emotion getEmotionType();
}
