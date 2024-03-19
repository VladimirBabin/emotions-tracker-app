package com.specificgroup.emotionstracker.alerts.alert.emotionalertprocessors;

import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType;
import org.springframework.stereotype.Component;

@Component
public class FiveTimesAWeekStressedProcessor extends TimeBasedEmotionAlertProcessor {
    @Override
    public int checkedDaysPeriod() {
        return 7;
    }

    @Override
    public int numberOfRequiredRepetitions() {
        return 5;
    }

    @Override
    public EmotionAlertType emotionAlertType() {
        return EmotionAlertType.STRESSED_FIVE_TIMES_LAST_WEEK;
    }
}
