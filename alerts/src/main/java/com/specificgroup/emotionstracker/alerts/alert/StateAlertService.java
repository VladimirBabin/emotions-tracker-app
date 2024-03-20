package com.specificgroup.emotionstracker.alerts.alert;

import com.specificgroup.emotionstracker.alerts.alert.domain.StateAlertType;
import com.specificgroup.emotionstracker.alerts.state.StateLoggedEvent;

import java.util.List;

public interface StateAlertService {

    /**
     * @return alerts generated during the last minute before the call.
     */
    List<StateAlertType> getLastAddedStateAlerts(Long userId);

    /**
     * Accepts a new state log from a user.
     * @param stateLoggedEvent event containing data about state logged.
     */
    void newTriggeringStateForUser(StateLoggedEvent stateLoggedEvent);
}
