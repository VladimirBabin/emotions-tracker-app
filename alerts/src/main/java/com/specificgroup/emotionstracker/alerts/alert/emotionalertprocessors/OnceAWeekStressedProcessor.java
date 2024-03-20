package com.specificgroup.emotionstracker.alerts.alert.emotionalertprocessors;

import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType;
import com.specificgroup.emotionstracker.alerts.state.Emotion;
import org.springframework.stereotype.Component;

@Component
public class OnceAWeekStressedProcessor extends TimeBasedEmotionAlertProcessor {
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
        return 1;
    }

    @Override
    public EmotionAlertType emotionAlertType() {
        return EmotionAlertType.STRESSED_ONCE_A_WEEK;
    }
}
