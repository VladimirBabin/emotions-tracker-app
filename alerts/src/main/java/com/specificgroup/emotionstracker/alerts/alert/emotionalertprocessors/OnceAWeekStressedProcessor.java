package com.specificgroup.emotionstracker.alerts.alert.emotionalertprocessors;

import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType;

public class OnceAWeekStressedProcessor extends TimeBasedEmotionAlertProcessor {
    @Override
    public int checkedDaysPeriod() {
        return 7;
    }

    @Override
    public int numberOfRequiredRepetitions() {
        return 1;
    }

    @Override
    public EmotionAlertType emotionAlertType() {
        return EmotionAlertType.STRESSED_ONCE_A_WEEK;
    }
}
