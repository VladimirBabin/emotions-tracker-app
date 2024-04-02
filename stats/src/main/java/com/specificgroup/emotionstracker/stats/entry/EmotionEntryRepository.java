package com.specificgroup.emotionstracker.stats.entry;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface EmotionEntryRepository extends CrudRepository<EmotionEntry, Long> {
    /**
     * Retrieves emotions for user after given date time.
     */
    @Query("SELECT e FROM EmotionEntry e GROUP BY e.emotion ORDER BY e.emotion DESC")
    List<EmotionEntry> findTopRepeatedEmotionEntriesGropedByEmotionsDesc(String userId, LocalDateTime dateTime);
}
