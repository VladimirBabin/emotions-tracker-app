package com.specificgroup.emotionstracker.alerts.alert;

import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType;
import com.specificgroup.emotionstracker.alerts.alert.domain.StateAlertType;
import com.specificgroup.emotionstracker.alerts.state.EmotionLoggedEvent;
import com.specificgroup.emotionstracker.alerts.state.StateLoggedEvent;

import java.util.List;

public interface AlertService {

    /**
     * @return alerts generated during the last minute before the call.
     */
    List<StateAlertType> getLastMinuteAddedStateAlerts(Long userId);
    List<EmotionAlertType> getLastMinuteAddedEmotionAlerts(Long userId);

    /**
     * Accepts a new state log from a user.
     * @param stateLoggedEvent event containing data about state logged.
     */
    void newTriggeringStateForUser(StateLoggedEvent stateLoggedEvent);

    /**
     * Accepts a new emotion log from a user.
     * @param emotionLoggedEvent event containing data about emotion logged.
     */
    void newTriggeringEmotionForUser(EmotionLoggedEvent emotionLoggedEvent);
}
