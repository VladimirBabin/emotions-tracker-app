package com.specificgroup.emotionstracker.alerts.alert.statealertprocessors;

import com.specificgroup.emotionstracker.alerts.alert.domain.StateAlertType;
import org.springframework.stereotype.Component;

@Component
public class TwiceInTwoDaysStateAlertProcessor extends TimeBasedStateAlertProcessor {
    @Override
    public int checkedDaysPeriod() {
        return 2;
    }

    @Override
    public int numberOfRequiredRepetitions() {
        return 2;
    }

    @Override
    public StateAlertType stateAlertType() {
        return StateAlertType.LOW_STATE_TWICE_IN_TWO_DAYS;
    }
}
