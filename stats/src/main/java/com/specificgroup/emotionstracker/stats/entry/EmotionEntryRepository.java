package com.specificgroup.emotionstracker.stats.entry;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EmotionEntryRepository extends CrudRepository<EmotionEntry, Long> {
    /**
     * Retrieves most logged emotions for user after given date time.
     */
    @Query("SELECT e.emotion FROM EmotionEntry e WHERE e.userId = ?1 AND e.dateTime > ?2 " +
            "GROUP BY e.emotion ORDER BY COUNT(e.emotion) DESC")
    List<Emotion> findTopRepeatedEmotionEntriesGropedByEmotionsDesc(String userId, LocalDateTime dateTime);
}
