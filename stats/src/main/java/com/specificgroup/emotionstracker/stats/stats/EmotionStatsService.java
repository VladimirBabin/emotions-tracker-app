package com.specificgroup.emotionstracker.stats.stats;

import com.specificgroup.emotionstracker.stats.entry.Emotion;
import com.specificgroup.emotionstracker.stats.entry.EmotionLoggedEvent;

import java.util.List;

public interface EmotionStatsService {
    int TOP_EMOTIONS_LIMIT = 5;

    /**
     * Accepts a new emotion log from a user.
     * @param emotionLoggedEvent event containing data about emotion logged.
     */
    void newEmotionLogForUser(EmotionLoggedEvent emotionLoggedEvent);

    /**
     * Returns a sorted list of TOP_EMOTIONS_LIMIT number of most logged emotions
     * for given period of time defined in implementation.
     */
    List<Emotion> getLastWeekMostLoggedEmotions(String userId);

    /**
     * Removes entry-related logged emotions from entry repository.
     * The alerts from alert repository aren't deleted because we don't want to show them again after they were shown.
     * @param entryId id of the entry
     */
    void removeEntryRelatedData(Long entryId);
}
