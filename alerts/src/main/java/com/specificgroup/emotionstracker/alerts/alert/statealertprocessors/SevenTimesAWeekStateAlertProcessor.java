package com.specificgroup.emotionstracker.alerts.alert.statealertprocessors;

import com.specificgroup.emotionstracker.alerts.alert.domain.StateAlertType;
import org.springframework.stereotype.Component;

@Component
public class SevenTimesAWeekStateAlertProcessor extends TimeBasedStateAlertProcessor {

    @Override
    public int checkedDaysPeriod() {
        return 7;
    }

    @Override
    public int numberOfRequiredRepetitions() {
        return 7;
    }

    @Override
    public StateAlertType stateAlertType() {
        return StateAlertType.LOW_STATE_SEVEN_TIMES_A_WEEK;
    }
}
