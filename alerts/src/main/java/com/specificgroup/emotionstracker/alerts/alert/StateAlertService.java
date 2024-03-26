package com.specificgroup.emotionstracker.alerts.alert;

import com.specificgroup.emotionstracker.alerts.state.StateLoggedEvent;

import java.util.List;

public interface StateAlertService {

    /**
     * @return not shown yet alerts generated in a custom set timeframe and sets alerts as shown.
     */
    List<String> getLastAddedStateAlerts(Long userId);

    /**
     * Accepts a new state log from a user.
     * @param stateLoggedEvent event containing data about state logged.
     */
    void newTriggeringStateForUser(StateLoggedEvent stateLoggedEvent);
}
