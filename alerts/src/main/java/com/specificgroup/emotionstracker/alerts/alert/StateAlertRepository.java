package com.specificgroup.emotionstracker.alerts.alert;


import com.specificgroup.emotionstracker.alerts.alert.domain.StateAlert;
import com.specificgroup.emotionstracker.alerts.alert.domain.StateAlertType;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface StateAlertRepository extends CrudRepository<StateAlert, Long> {

    /**
     * Gets alert types for user found by id after specified LocalDateTime.
     */
    @Query("SELECT s.stateAlertType FROM StateAlert s WHERE s.userId = :userId AND s.localDateTime > :localDateTime")
    List<StateAlertType> getAlertTypesByUserIdAfterGivenLocalDateTime(@Param("userId") long userId,
                                                                      LocalDateTime localDateTime);

    @Query("SELECT DISTINCT(s) FROM StateAlert s WHERE s.userId = :userId AND s.localDateTime > :localDateTime")
    List<StateAlert> getAlertsByUserIdAfterGivenLocalDateTime(@Param("userId") long userId,
                                                             LocalDateTime localDateTime);
}
