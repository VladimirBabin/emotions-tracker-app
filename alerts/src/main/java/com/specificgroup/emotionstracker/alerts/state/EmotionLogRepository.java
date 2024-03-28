package com.specificgroup.emotionstracker.alerts.state;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface EmotionLogRepository extends CrudRepository<EmotionLog, Long> {
    /**
     * Retrieves all the emotions logs for user identified by user id.
     */
    List<EmotionLog> findByUserIdAndEmotionOrderByDateTime(String userId, Emotion emotion);
}
