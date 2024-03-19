package com.specificgroup.emotionstracker.alerts.alert.emotionalertprocessors;

import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlert;
import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType;
import com.specificgroup.emotionstracker.alerts.state.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EmotionAlertProcessor {
    /**
     * Checks if the user's last logged emotion should trigger an alert.
     * @return an EmotionAlertType triggered by the emotion logged.
     */
    Optional<EmotionAlertType> processForOptionalAlert(List<EmotionLog> allLoggedEmotions,
                                                       List<EmotionAlert> lastMonthEmotionAlerts);

    default Optional<EmotionAlertType> processForOptionalAlertWithCheck(List<EmotionLog> allLoggedEmotions,
                                                                List<EmotionAlert> lastMonthEmotionAlerts) {
        if (userAlreadyReceivedAlertInCustomPeriod(lastMonthEmotionAlerts)) {
            return Optional.empty();
        }
        return this.processForOptionalAlert(allLoggedEmotions, lastMonthEmotionAlerts);
    }

    int checkedDaysPeriod();

    private boolean userAlreadyReceivedAlertInCustomPeriod(List<EmotionAlert> lastMonthEmotionAlerts) {
        return lastMonthEmotionAlerts.stream()
                .anyMatch(ea -> ea.getLocalDateTime().isAfter(LocalDateTime.now().minusDays(checkedDaysPeriod())));
    }
}
