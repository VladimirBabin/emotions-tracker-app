package com.specificgroup.emotionstracker.alerts.alert.emotionalertprocessors;

import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType;
import com.specificgroup.emotionstracker.alerts.entry.Emotion;
import org.springframework.stereotype.Component;

@Component
public class FiveTimesAWeekStressedProcessor extends TimeBasedEmotionAlertProcessor {
    @Override
    public int checkedDaysPeriod() {
        return 7;
    }

    @Override
    public Emotion getEmotionType() {
        return Emotion.STRESSED;
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
