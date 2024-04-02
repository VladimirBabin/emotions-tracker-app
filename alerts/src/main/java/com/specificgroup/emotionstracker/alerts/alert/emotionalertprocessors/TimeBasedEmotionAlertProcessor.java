package com.specificgroup.emotionstracker.alerts.alert.emotionalertprocessors;

import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlert;
import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType;
import com.specificgroup.emotionstracker.alerts.entry.EmotionEntry;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public abstract class TimeBasedEmotionAlertProcessor implements EmotionAlertProcessor {
    @Override
    public Optional<EmotionAlertType> processForOptionalAlert(List<EmotionEntry> allLoggedEmotions,
                                                              List<EmotionAlert> lastEmotionAlerts) {
        long count = allLoggedEmotions.stream()
                .filter(sl -> sl.getDateTime().isAfter(LocalDateTime.now().minusDays(checkedDaysPeriod())))
                .count();
        if (count >= numberOfRequiredRepetitions()) {
            return Optional.of(emotionAlertType());
        }
        return Optional.empty();
    }

    public abstract EmotionAlertType emotionAlertType();
    public abstract int numberOfRequiredRepetitions();
}
