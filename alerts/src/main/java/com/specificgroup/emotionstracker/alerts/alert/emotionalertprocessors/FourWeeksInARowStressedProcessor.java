package com.specificgroup.emotionstracker.alerts.alert.emotionalertprocessors;

import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlert;
import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType;
import com.specificgroup.emotionstracker.alerts.entry.Emotion;
import com.specificgroup.emotionstracker.alerts.entry.EmotionLog;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class FourWeeksInARowStressedProcessor implements EmotionAlertProcessor {

    @Override
    public Emotion getEmotionType() {
        return Emotion.STRESSED;
    }
    @Override
    public Optional<EmotionAlertType> processForOptionalAlert(List<EmotionLog> allLoggedEmotions,
                                                              List<EmotionAlert> lastEmotionAlerts) {
        long count = lastEmotionAlerts.stream()
                .filter(alert -> alert.getEmotionAlertType().equals(EmotionAlertType.STRESSED_ONCE_A_WEEK))
                .count();
        if (count >= 4) {
            return Optional.of(EmotionAlertType.STRESSED_FOUR_WEEKS_IN_A_ROW);
        }
        return Optional.empty();
    }

    @Override
    public int checkedDaysPeriod() {
        return 30;
    }

    public EmotionAlertType emotionAlertType() {
        return EmotionAlertType.STRESSED_FOUR_WEEKS_IN_A_ROW;
    }
}
