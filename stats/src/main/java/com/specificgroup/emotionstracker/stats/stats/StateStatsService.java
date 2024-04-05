package com.specificgroup.emotionstracker.stats.stats;

import com.specificgroup.emotionstracker.stats.entry.StateLoggedEvent;

public interface StateStatsService {

    /**
     * Accepts a new state log from a user.
     * @param stateLoggedEvent event containing data about state logged.
     */
    void newStateLogForUser(StateLoggedEvent stateLoggedEvent);

    /**
     * Retrieves statistics about the states logged during the last week for a particular user.
     * @return the resulting in query WeeklyStats object.
     */
    WeeklyStats getWeeklyStatsForUser(String userId);

    /**
     * Removes entry-related logged states from entry repository.
     * The alerts from alert repository aren't deleted because we don't want to show them again after they were shown.
     * @param entryId id of the entry
     */
    void removeEntryRelatedData(Long entryId);
}
