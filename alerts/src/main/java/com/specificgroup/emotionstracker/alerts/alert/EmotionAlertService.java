package com.specificgroup.emotionstracker.alerts.alert;

import com.specificgroup.emotionstracker.alerts.entry.EmotionLoggedEvent;

import java.util.List;

public interface EmotionAlertService {

    /**
     * @return not shown yet alerts generated in a custom set timeframe and sets alerts as shown.
     */
    List<String> getLastAddedEmotionAlerts(String userId);

    /**
     * Accepts a new emotion log from a user.
     * @param emotionLoggedEvent event containing data about emotion logged.
     */
    void newTriggeringEmotionForUser(EmotionLoggedEvent emotionLoggedEvent);

    /**
     * Removes entry-related logged emotions from entry repository.
     * The alerts from alert repository aren't deleted because we don't want to show them again after they were shown.
     * @param entryId id of the entry
     */
    void removeEntryRelatedData(Long entryId);
}
