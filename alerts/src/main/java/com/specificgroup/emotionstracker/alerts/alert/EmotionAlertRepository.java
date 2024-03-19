package com.specificgroup.emotionstracker.alerts.alert;

import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlert;
import com.specificgroup.emotionstracker.alerts.alert.domain.EmotionAlertType;
import com.specificgroup.emotionstracker.alerts.alert.domain.StateAlert;
import com.specificgroup.emotionstracker.alerts.alert.domain.StateAlertType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface EmotionAlertRepository extends CrudRepository<EmotionAlert, Long> {
    /**
     * Gets emotion alert types for user found by id after specified LocalDateTime.
     */
    @Query("SELECT e.emotionAlertType FROM EmotionAlert e WHERE e.userId = :userId AND e.localDateTime > :localDateTime")
    List<EmotionAlertType> getAlertTypesByUserIdAfterGivenLocalDateTime(@Param("userId") long userId,
                                                                    LocalDateTime localDateTime);

    @Query("SELECT DISTINCT(e) FROM EmotionAlert e WHERE e.userId = :userId AND e.localDateTime > :localDateTime")
    List<EmotionAlert> getAlertsByUserIdAfterGivenLocalDateTime(@Param("userId") long userId,
                                                             LocalDateTime localDateTime);
}
