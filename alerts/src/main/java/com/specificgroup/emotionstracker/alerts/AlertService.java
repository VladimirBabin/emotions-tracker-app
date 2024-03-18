package com.specificgroup.emotionstracker.alerts;

import com.specificgroup.emotionstracker.alerts.state.EmotionLoggedEvent;
import com.specificgroup.emotionstracker.alerts.state.StateLoggedEvent;

public interface AlertService {

    /**
     * Accepts a new state log from a user.
     * @param stateLoggedEvent event containing data about state logged.
     */
    void newStateLogForUser(StateLoggedEvent stateLoggedEvent);

    /**
     * Accepts a new emotion log from a user.
     * @param emotionLoggedEvent event containing data about emotion logged.
     */
    void newEmotionLogForUser(EmotionLoggedEvent emotionLoggedEvent);
}
