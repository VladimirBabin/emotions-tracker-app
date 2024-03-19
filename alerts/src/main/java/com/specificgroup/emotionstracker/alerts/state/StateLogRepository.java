package com.specificgroup.emotionstracker.alerts.state;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StateLogRepository extends CrudRepository<StateLog, Long> {

    /**
     * Retrieves all the state logs for user identified by user id.
     */
    List<StateLog> findByUserOrderByDateTime(long userId);
}
