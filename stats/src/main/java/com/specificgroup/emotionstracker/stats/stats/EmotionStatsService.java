package com.specificgroup.emotionstracker.stats.stats;

import com.specificgroup.emotionstracker.stats.entry.Emotion;
import com.specificgroup.emotionstracker.stats.entry.EmotionLoggedEvent;

import java.util.Set;

public interface EmotionStatsService {

    /**
     * Accepts a new emotion log from a user.
     * @param emotionLoggedEvent event containing data about emotion logged.
     */
    void newEmotionLogForUser(EmotionLoggedEvent emotionLoggedEvent);

    Set<Emotion> getLastWeekMostLoggedEmotions(String userId);
}
