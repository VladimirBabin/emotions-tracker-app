package com.specificgroup.emotionstracker.alerts.alert;

import com.specificgroup.emotionstracker.alerts.state.EmotionLoggedEvent;

import java.util.List;

public interface EmotionAlertService {

    /**
     * @return not shown yet alerts generated in a custom set timeframe and sets alerts as shown.
     */
    List<String> getLastAddedEmotionAlerts(Long userId);

    /**
     * Accepts a new emotion log from a user.
     * @param emotionLoggedEvent event containing data about emotion logged.
     */
    void newTriggeringEmotionForUser(EmotionLoggedEvent emotionLoggedEvent);
}
