package com.specificgroup.emotionstracker.alerts.alert;

import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlert;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface EmotionAlertRepository extends CrudRepository<EmotionAlert, Long> {
    /**
     * Gets not shown yet emotion alerts for user found by id after specified LocalDateTime.
     */
    @Query("SELECT DISTINCT(e) FROM EmotionAlert e " +
            "WHERE e.userId = :userId AND e.localDateTime > :localDateTime AND e.shown = FALSE")
    List<EmotionAlert> getNotShownAlertsByUserIdAfterGivenLocalDateTime(@Param("userId") String userId,
                                                                LocalDateTime localDateTime);

    @Query("SELECT DISTINCT(e) FROM EmotionAlert e WHERE e.userId = :userId AND e.localDateTime > :localDateTime")
    List<EmotionAlert> getAlertsByUserIdAfterGivenLocalDateTime(@Param("userId") String userId,
                                                             LocalDateTime localDateTime);
}
