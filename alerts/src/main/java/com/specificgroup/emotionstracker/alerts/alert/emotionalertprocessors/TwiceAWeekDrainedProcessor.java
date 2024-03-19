package com.specificgroup.emotionstracker.alerts.alert.emotionalertprocessors;

import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType;
import org.springframework.stereotype.Component;

@Component
public class TwiceAWeekDrainedProcessor extends TimeBasedEmotionAlertProcessor {
    @Override
    public int checkedDaysPeriod() {
        return 7;
    }

    @Override
    public int numberOfRequiredRepetitions() {
        return 2;
    }

    @Override
    public EmotionAlertType emotionAlertType() {
        return EmotionAlertType.DRAINED_TWICE_LAST_WEEK;
    }
}
