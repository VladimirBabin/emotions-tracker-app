package com.specificgroup.emotionstracker.alerts.alert;

import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType;
import com.specificgroup.emotionstracker.alerts.state.EmotionLoggedEvent;

import java.util.List;

public interface EmotionAlertService {

    /**
     * @return alerts generated during the last minute before the call.
     */
    List<EmotionAlertType> getLastAddedEmotionAlerts(Long userId);

    /**
     * Accepts a new emotion log from a user.
     * @param emotionLoggedEvent event containing data about emotion logged.
     */
    void newTriggeringEmotionForUser(EmotionLoggedEvent emotionLoggedEvent);
}
