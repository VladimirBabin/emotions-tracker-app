package com.specificgroup.emotionstracker.alerts.alert.statealertprocessors;

import com.specificgroup.emotionstracker.alerts.alert.domain.StateAlert;
import com.specificgroup.emotionstracker.alerts.alert.domain.StateAlertType;
import com.specificgroup.emotionstracker.alerts.entry.StateEntry;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public abstract class TimeBasedStateAlertProcessor implements StateAlertProcessor{
    @Override
    public Optional<StateAlertType> processForOptionalAlert(List<StateEntry> allLoggedStates,
                                                            List<StateAlert> lastStateAlerts) {
        long count = allLoggedStates.stream()
                .filter(sl -> sl.getDateTime().isAfter(LocalDateTime.now().minusDays(checkedDaysPeriod())))
                .count();
        if (count >= numberOfRequiredRepetitions()) {
            return Optional.of(stateAlertType());
        }
        return Optional.empty();
    }

    public abstract int numberOfRequiredRepetitions();

    public abstract StateAlertType stateAlertType();
}
