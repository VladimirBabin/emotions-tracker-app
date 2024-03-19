package com.specificgroup.emotionstracker.alerts.alert.statealertprocessors;

import com.specificgroup.emotionstracker.alerts.alert.domain.StateAlert;
import com.specificgroup.emotionstracker.alerts.alert.domain.StateAlertType;
import com.specificgroup.emotionstracker.alerts.state.StateLog;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StateAlertProcessor {
    /**
     * Checks if the user's last logged state should trigger an alert.
     *
     * @return an StateAlertType triggered by the emotion logged.
     */
    Optional<StateAlertType> processForOptionalAlert(List<StateLog> allLoggedStates,
                                                     List<StateAlert> lastMonthStateAlerts);

    default Optional<StateAlertType> processForOptionalAlertWithCheck(List<StateLog> allLoggedStates,
                                                                      List<StateAlert> lastMonthStateAlerts) {
        if (userAlreadyReceivedAlertInCustomPeriod(lastMonthStateAlerts)) {
            return Optional.empty();
        }
        return this.processForOptionalAlert(allLoggedStates, lastMonthStateAlerts);
    }

    int checkedDaysPeriod();

    private boolean userAlreadyReceivedAlertInCustomPeriod(List<StateAlert> lastMonthStateAlerts) {
        return lastMonthStateAlerts.stream()
                .anyMatch(sa -> sa.getLocalDateTime().isAfter(LocalDateTime.now().minusDays(checkedDaysPeriod())));
    }
}
