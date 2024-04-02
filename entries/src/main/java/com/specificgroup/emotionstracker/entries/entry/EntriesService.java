package com.specificgroup.emotionstracker.entries.entry;

import com.specificgroup.emotionstracker.entries.entry.domain.Entry;
import com.specificgroup.emotionstracker.entries.entry.dto.EntryDto;

import java.util.List;

public interface EntriesService {

    /**
     * Accepts a new state log coming from presentation layer.
     * @return the resulting StateLog object.
     */
    Entry acceptNewEntry(EntryDto entryDto);

    /**
     * Retrieves the last 10 logged states for a particular user.
     */
    List<Entry> getLastLogsForUser(String userId);

    /**
     * Retrieves statistics about the states logged during the last week for a particular user.
     * @return the resulting in query WeeklyStats object.
     */
    WeeklyStats getWeeklyStatsForUser(String userId);
}