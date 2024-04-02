package com.specificgroup.emotionstracker.alerts.entry;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface StateEntryRepository extends CrudRepository<StateEntry, Long> {

    /**
     * Retrieves all the state logs for user identified by user id.
     */
    List<StateEntry> findByUserIdOrderByDateTime(String userId);
}
