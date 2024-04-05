package com.specificgroup.emotionstracker.alerts.alert;

import com.specificgroup.emotionstracker.alerts.entry.StateLoggedEvent;

import java.util.List;

public interface StateAlertService {

    /**
     * @return not shown yet alerts generated in a custom set timeframe and sets alerts as shown.
     */
    List<String> getLastAddedStateAlerts(String userId);

    /**
     * Accepts a new state log from a user.
     * @param stateLoggedEvent event containing data about state logged.
     */
    void newTriggeringStateForUser(StateLoggedEvent stateLoggedEvent);

    /**
     * Removes entry-related logged states from entry repository.
     * The alerts from alert repository aren't deleted because we don't want to show them again after they were shown.
     * @param entryId id of the entry
     */
    void removeEntryRelatedData(Long entryId);
}
