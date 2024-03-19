package com.specificgroup.emotionstracker.alerts.alert.emotionalertprocessors;

import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlert;
import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmotionAlertSecondaryProcessor {
    /**
     * Checks if the user's newly received alert should trigger a secondary alert.
     * @return an EmotionAlertType triggered by new alert generated.
     */
    Optional<EmotionAlertType> processForOptionalAlert(List<EmotionAlert> lastMonthEmotionAlerts);

    default Optional<EmotionAlertType> processForOptionalAlertWithCheck(List<EmotionAlert> lastMonthEmotionAlerts) {
        if (userAlreadyReceivedAlertInCustomPeriod(lastMonthEmotionAlerts)) {
            return Optional.empty();
        }
        return this.processForOptionalAlert(lastMonthEmotionAlerts);
    }

    int checkedDaysPeriod();

    EmotionAlertType emotionAlertType();

    private boolean userAlreadyReceivedAlertInCustomPeriod(List<EmotionAlert> lastMonthEmotionAlerts) {
        return lastMonthEmotionAlerts.stream()
                .filter(ea -> ea.getEmotionAlertType().equals(emotionAlertType()))
                .anyMatch(ea -> ea.getLocalDateTime().isAfter(LocalDateTime.now().minusDays(checkedDaysPeriod())));
    }
}
