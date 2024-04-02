package com.specificgroup.emotionstracker.alerts.entry;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmotionEntryRepository extends CrudRepository<EmotionEntry, Long> {
    /**
     * Retrieves all the emotions logs for user identified by user id.
     */
    List<EmotionEntry> findByUserIdAndEmotionOrderByDateTime(String userId, Emotion emotion);
}
