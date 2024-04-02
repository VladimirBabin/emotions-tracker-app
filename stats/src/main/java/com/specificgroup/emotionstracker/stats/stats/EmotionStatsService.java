package com.specificgroup.emotionstracker.stats.stats;

import com.specificgroup.emotionstracker.stats.entry.EmotionLoggedEvent;

public interface EmotionStatsService {

    /**
     * Accepts a new emotion log from a user.
     * @param emotionLoggedEvent event containing data about emotion logged.
     */
    void newEmotionLogForUser(EmotionLoggedEvent emotionLoggedEvent);
}
