package com.specificgroup.emotionstracker.alerts.alert.emotionalertprocessors;

import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType;
import com.specificgroup.emotionstracker.alerts.state.Emotion;
import org.springframework.stereotype.Component;

@Component
public class TwiceAWeekDepressedProcessor extends TimeBasedEmotionAlertProcessor {
    @Override
    public int checkedDaysPeriod() {
        return 7;
    }

    @Override
    public Emotion getEmotionType() {
        return Emotion.SAD;
    }

    @Override
    public int numberOfRequiredRepetitions() {
        return 2;
    }

    @Override
    public EmotionAlertType emotionAlertType() {
        return EmotionAlertType.DEPRESSED_TWICE_LAST_WEEK;
    }
}
