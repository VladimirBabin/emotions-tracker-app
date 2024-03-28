package com.specificgroup.emotionstracker.entrylogging.state;

import com.specificgroup.emotionstracker.entrylogging.state.domain.EntryLog;

import java.util.List;

public interface EntryLogService {

    /**
     * Accepts a new state log coming from presentation layer.
     * @return the resulting StateLog object.
     */
    EntryLog acceptNewEntry(EntryLogDTO entryLogDTO);

    /**
     * Retrieves the last 10 logged states for a particular user.
     */
    List<EntryLog> getLastLogsForUser(String userId);

    /**
     * Retrieves statistics about the states logged during the last week for a particular user.
     * @return the resulting in query WeeklyStats object.
     */
    WeeklyStats getWeeklyStatsForUser(String userId);
}
