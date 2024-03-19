package com.specificgroup.emotionstracker.alerts.alert.emotionalertprocessors;

import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType;
import org.springframework.stereotype.Component;

@Component
public class TwiceAWeekScaredProcessor extends TimeBasedEmotionAlertProcessor {
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
        return EmotionAlertType.SCARED_TWICE_LAST_WEEK;
    }
}
